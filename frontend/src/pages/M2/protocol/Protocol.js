import React, { PureComponent } from 'react';
import { connect } from 'dva';
import {
  Table,
  Button,
  Row,
  Col,
  Switch,
  Icon,
  Input,
  Tag,
  Popconfirm,
  Modal,
  message,
  Tabs,
} from 'antd';
import GridContent from '@/components/PageHeaderWrapper/GridContent';
import Link from 'umi/link';
import CreateDialog from './CreateDialog';
import { splitLongText, toHexString } from '@/utils/utils';

const { TabPane } = Tabs;

@connect(({ m2Protocol, loading }) => ({
  m2Protocol,
  loading,
}))
class Protocol extends PureComponent {
  state = {
    createDialogVisible: false,
    editMode: false,
    editRecord: {},
    category: 1,
    currentPage: 1,
    pageSize: 10,
    orderName: 'type',
    orderDirection: 'asc',
    deleteProtocolId: new Set(),
    filters: {},
  };

  componentDidMount() {
    this.list();
  }

  list = () => {
    const { dispatch } = this.props;
    const { category, currentPage, pageSize, orderName, orderDirection, filters } = this.state;
    dispatch({
      type: 'm2Protocol/list',
      payload: {
        category,
        currentPage,
        pageSize,
        orderName,
        orderDirection,
        ...filters,
      },
    });
  };

  onTableChange = (pagination, filters, sorter) => {
    const { current, pageSize } = pagination;
    const orderDirection = sorter.order === 'descend' ? 'desc' : 'asc';
    this.setState(
      {
        currentPage: current,
        pageSize,
        orderName: sorter.field,
        orderDirection,
      },
      this.list
    );
  };

  getColumns = category => {
    const columns = [];
    columns.push({
      title: 'TYPE',
      dataIndex: 'type',
      key: 'type',
      width: 40,
      render: text => toHexString(text, 8),
      sorter: true,
    });
    if (category !== 1) {
      columns.push({
        title: 'Protocol Name',
        dataIndex: 'protocolEnName',
        key: 'protocolEnName',
        width: 120,
      });
    }
    columns.push({
      title: 'Name(CN)',
      dataIndex: 'cnName',
      key: 'cnName',
      width: 120,
    });
    columns.push({
      title: 'Name(EN)',
      dataIndex: 'enName',
      key: 'enName',
      width: 120,
      render: (text, record) => <Link to={`/m2/field/${record.id}/${record.enName}`}>{text}</Link>,
    });
    columns.push({
      title: 'Description',
      dataIndex: 'description',
      key: 'description',
      width: 360,
      render: text => splitLongText(text),
    });
    columns.push({
      title: 'Option',
      key: 'option',
      width: 120,
      render: (text, record) => {
        const { deleteProtocolId } = this.state;
        const deleting = deleteProtocolId.has(record.id);
        const popParams = {};
        if (deleting || record.deleted) {
          popParams.visible = false;
        }
        return (
          <div>
            {!(deleting || record.deleted) && (
              <Button
                style={{ cursor: 'pointer', marginRight: 8 }}
                type="primary"
                size="small"
                icon="edit"
                onClick={() => this.edit(record)}
              >
                {'Edit'}
              </Button>
            )}
            <Popconfirm
              title="Are you sure delete this protocol?"
              onConfirm={() => {
                this.delete(record.id);
              }}
              okText="Yes"
              cancelText="No"
              {...popParams}
            >
              <Button
                style={{ cursor: 'pointer' }}
                type="danger"
                size="small"
                icon="delete"
                disabled={deleting || record.deleted}
                loading={deleting}
              >
                {record.deleted ? 'Deleted' : 'Delete'}
              </Button>
            </Popconfirm>
          </div>
        );
      },
    });
    return columns;
  };

  edit = record => {
    this.setState(
      {
        editMode: true,
        editRecord: record,
      },
      () => this.changeCreateDialogVisible(true)
    );
  };

  create = () => {
    this.setState(
      {
        editMode: false,
        editRecord: {},
      },
      () => this.changeCreateDialogVisible(true)
    );
  };

  delete = id => {
    const { dispatch } = this.props;
    const { deleteProtocolId } = this.state;
    this.setState({
      deleteProtocolId: deleteProtocolId.add(id),
    });
    dispatch({
      type: 'm2Protocol/deleteById',
      payload: {
        id,
      },
      callback: () => {
        deleteProtocolId.delete(id);
        this.setState({
          deleteProtocolId,
        });
      },
    });
  };

  changeCreateDialogVisible = visible => {
    this.setState({
      createDialogVisible: visible,
    });
  };

  createOver = () => {
    this.changeCreateDialogVisible(false);
    this.list();
  };

  changeTab = k => {
    this.setState(
      {
        category: Number(k),
        currentPage: 1,
        pageSize: 10,
      },
      this.list
    );
  };

  getCategoryName = () => {
    const { category } = this.state;
    if (category === 1) {
      return 'Protocol';
    }
    return category === 2 ? 'TLV' : 'Extractor';
  };

  render() {
    const { m2Protocol, loading } = this.props;
    const { list, total } = m2Protocol;
    const {
      category,
      currentPage,
      pageSize,
      createDialogVisible,
      editRecord,
      editMode,
    } = this.state;

    return (
      <GridContent>
        <Tabs defaultActiveKey="1" onChange={this.changeTab}>
          <TabPane tab="Protocol" key="1" />
          <TabPane tab="TLV" key="2" />
          <TabPane tab="Extractor" key="3" />
        </Tabs>
        <Row gutter={24} style={{ marginBottom: 12 }}>
          <Col span={24}>
            <Button
              type="primary"
              onClick={() => this.create()}
            >{`Create ${this.getCategoryName()}`}</Button>
          </Col>
        </Row>
        <Table
          columns={this.getColumns(category)}
          dataSource={list}
          bordered
          size="middle"
          rowKey="tableName"
          loading={loading.effects['m2Protocol/list']}
          pagination={{
            total,
            showSizeChanger: true,
            showQuickJumper: true,
            size: 'middle',
            showTotal: t => `Total ${t} items`,
            pageSizeOptions: ['10', '30', '50'],
            current: currentPage,
            pageSize,
          }}
          onChange={this.onTableChange}
        />
        <Modal
          title="Create Protocol"
          visible={createDialogVisible}
          onCancel={() => this.changeCreateDialogVisible(false)}
          footer={null}
          destroyOnClose
        >
          <CreateDialog
            createOver={this.createOver}
            record={editRecord}
            editMode={editMode}
            category={category}
          />
        </Modal>
      </GridContent>
    );
  }
}

export default Protocol;
