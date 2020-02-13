import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Table, Button, Row, Col, Switch, Icon, Modal } from 'antd';
import GridContent from '@/components/PageHeaderWrapper/GridContent';
import CreateDialog from './CreateDialog';

@connect(({ m2Type, loading }) => ({
  m2Type,
  loading,
}))
class Protocol extends PureComponent {
  state = {
    createDialogVisible: false,
    changeActiveTypeId: new Set(),
  };

  componentDidMount() {
    this.list();
  }

  list = () => {
    const { dispatch } = this.props;
    dispatch({
      type: 'm2Type/list',
    });
  };

  isTlvOrStruct = record => {
    return record.cnName === 'TLV' || record.cnName === '结构体';
  };

  getColumns = () => {
    return [
      {
        title: '中文名',
        dataIndex: 'cnName',
        key: 'cnName',
        width: 160,
      },
      {
        title: '英文名',
        dataIndex: 'enName',
        key: 'enName',
        width: 160,
      },
      {
        title: '显示顺序',
        dataIndex: 'displayOrder',
        key: 'displayOrder',
        width: 160,
      },
      {
        title: '启用',
        dataIndex: 'active',
        key: 'active',
        width: 160,
        render: (text, record) => {
          const { changeActiveTypeId } = this.state;
          return (
            <Switch
              checkedChildren={<Icon type="check" />}
              unCheckedChildren={<Icon type="close" />}
              checked={text === 1}
              loading={changeActiveTypeId.has(record.id)}
              onClick={() => this.changeActive(text === 1 ? 2 : 1, record.id)}
              disabled={this.isTlvOrStruct(record)}
            />
          );
        },
      },
      {
        title: '操作',
        key: 'option',
        width: 160,
        render: (text, record) => (
          <Button
            style={{ cursor: 'pointer', marginRight: 8 }}
            type="primary"
            size="small"
            icon="edit"
            onClick={() => this.edit(record)}
          >
            {'编辑'}
          </Button>
        ),
      },
    ];
  };

  changeActive = (active, id) => {
    const { dispatch } = this.props;
    const { changeActiveTypeId } = this.state;
    this.setState({
      changeActiveTypeId: changeActiveTypeId.add(id),
    });
    dispatch({
      type: 'm2Type/changeActive',
      payload: {
        active,
        id,
      },
      callback: () => {
        changeActiveTypeId.delete(id);
        this.setState({
          changeActiveTypeId,
        });
      },
    });
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

  changeCreateDialogVisible = visible => {
    this.setState({
      createDialogVisible: visible,
    });
  };

  createOver = () => {
    this.changeCreateDialogVisible(false);
    this.list();
  };

  render() {
    const { m2Type, loading } = this.props;
    const { list } = m2Type;
    const { createDialogVisible, editRecord, editMode } = this.state;

    return (
      <GridContent>
        <Row gutter={24} style={{ marginBottom: 12 }}>
          <Col span={24}>
            <Button type="primary" onClick={() => this.create()}>
              新建类型
            </Button>
          </Col>
        </Row>
        <Table
          columns={this.getColumns()}
          dataSource={list}
          bordered
          size="small"
          rowKey="id"
          loading={loading.effects['m2Type/list']}
          pagination={false}
        />
        <Modal
          title={`${editMode ? '更新' : '新建'}类型`}
          visible={createDialogVisible}
          onCancel={() => this.changeCreateDialogVisible(false)}
          footer={null}
          destroyOnClose
        >
          <CreateDialog createOver={this.createOver} record={editRecord} editMode={editMode} />
        </Modal>
      </GridContent>
    );
  }
}

export default Protocol;
