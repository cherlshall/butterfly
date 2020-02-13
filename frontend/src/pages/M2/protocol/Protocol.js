import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Table, Button, Row, Col, Switch, Icon, Input, Popconfirm, Modal, Tabs } from 'antd';
import GridContent from '@/components/PageHeaderWrapper/GridContent';
import Highlighter from 'react-highlight-words';
// import Link from 'umi/link';
import router from 'umi/router';
import CreateDialog from './CreateDialog';
import { splitLongText, toHexString, getEveryFirst } from '@/utils/utils';

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
    changeActiveProtocolId: new Set(),
    filters: {},
  };

  componentDidMount() {
    this.list();
    this.listProtocolName();
  }

  listProtocolName = () => {
    const { dispatch } = this.props;
    dispatch({
      type: 'm2Protocol/listProtocolName',
      payload: {
        category: 1,
      },
    });
  };

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
    const { orderName } = this.state;
    const { current, pageSize } = pagination;
    const orderDirection = sorter.order === 'descend' ? 'desc' : 'asc';
    const fil = getEveryFirst(filters);
    fil.protocolId = fil.protocolEnName;
    fil.protocolEnName = undefined;
    this.setState(
      {
        currentPage: current,
        pageSize,
        orderName: sorter.field || orderName,
        orderDirection,
        filters: fil,
      },
      this.list
    );
  };

  getColumnSearchProps = dataIndex => ({
    filterDropdown: ({ setSelectedKeys, selectedKeys, confirm, clearFilters }) => (
      <div style={{ padding: 8 }}>
        <Input
          ref={node => {
            this.searchInput = node;
          }}
          placeholder={`Search ${dataIndex}`}
          value={selectedKeys[0]}
          onChange={e => setSelectedKeys(e.target.value ? [e.target.value] : [])}
          onPressEnter={() => this.handleSearch(selectedKeys, confirm, dataIndex)}
          style={{ width: 188, marginBottom: 8, display: 'block' }}
        />
        <Button
          type="primary"
          onClick={() => this.handleSearch(selectedKeys, confirm, dataIndex)}
          icon="search"
          size="small"
          style={{ width: 90, marginRight: 8 }}
        >
          Search
        </Button>
        <Button
          onClick={() => this.handleReset(clearFilters, dataIndex)}
          size="small"
          style={{ width: 90 }}
        >
          Reset
        </Button>
      </div>
    ),
    filterIcon: filtered => (
      <Icon type="search" style={{ color: filtered ? '#1890ff' : undefined }} />
    ),
    onFilterDropdownVisibleChange: visible => {
      if (visible) {
        setTimeout(() => this.searchInput.select());
      }
    },
    render: text => {
      const { searchedColumn, searchText } = this.state;
      if (text === null || text === undefined) {
        return text;
      }
      return searchedColumn === dataIndex ? (
        <Highlighter
          highlightStyle={{ backgroundColor: '#ffc069', padding: 0 }}
          searchWords={[searchText]}
          autoEscape
          textToHighlight={text.toString()}
        />
      ) : (
        text
      );
    },
  });

  handleSearch = (selectedKeys, confirm, dataIndex) => {
    confirm();
    this.setState({
      searchText: selectedKeys[0],
      searchedColumn: dataIndex,
    });
  };

  handleReset = (clearFilters, dataIndex) => {
    clearFilters();
    this.setState({ searchText: '' });
  };

  getProtocolFilter = () => {
    const { m2Protocol } = this.props;
    const { names } = m2Protocol;
    const filters = [];
    names.forEach(item => {
      filters.push({ text: item.enName, value: item.id });
    });
    return filters;
  };

  getColumns = category => {
    const columns = [];
    if (category === 1) {
      columns.push({
        title: 'TYPE',
        dataIndex: 'type',
        key: 'type',
        width: 40,
        render: text => toHexString(text, 8),
        sorter: true,
      });
    }
    if (category !== 1) {
      columns.push({
        title: 'Protocol Name',
        dataIndex: 'protocolEnName',
        key: 'protocolEnName',
        width: 160,
        filters: this.getProtocolFilter(),
        filterMultiple: false,
      });
    }
    columns.push({
      title: 'Name(CN)',
      dataIndex: 'cnName',
      key: 'cnName',
      width: 160,
      ...this.getColumnSearchProps('cnName'),
    });
    columns.push({
      title: 'Name(EN)',
      dataIndex: 'enName',
      key: 'enName',
      width: 160,
      ...this.getColumnSearchProps('enName'),
      // render: (text, record) => <Link to={`/m2/field/${record.id}/${record.enName}`}>{text}</Link>,
    });
    columns.push({
      title: 'Active',
      dataIndex: 'active',
      key: 'active',
      width: 40,
      render: (text, record) => {
        const { changeActiveProtocolId, deleteProtocolId } = this.state;
        return (
          <Switch
            checkedChildren={<Icon type="check" />}
            unCheckedChildren={<Icon type="close" />}
            checked={text === 1}
            loading={changeActiveProtocolId.has(record.id)}
            disabled={record.deleted || deleteProtocolId.has(record.id)}
            onClick={() => this.changeActive(text === 1 ? 2 : 1, record.id)}
          />
        );
      },
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
      width: category !== 1 ? 140 : 120,
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

  changeActive = (active, id) => {
    const { dispatch } = this.props;
    const { changeActiveProtocolId } = this.state;
    this.setState({
      changeActiveProtocolId: changeActiveProtocolId.add(id),
    });
    dispatch({
      type: 'm2Protocol/changeActive',
      payload: {
        active,
        id,
      },
      callback: () => {
        changeActiveProtocolId.delete(id);
        this.setState({
          changeActiveProtocolId,
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
    this.listProtocolName();
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
    return category === 2 ? 'TLV' : 'Struct';
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
          <TabPane tab="Struct" key="3" />
        </Tabs>
        <Row gutter={24} style={{ marginBottom: 12 }}>
          <Col span={24}>
            <Button type="primary" onClick={() => this.create()}>
              {`Create ${this.getCategoryName()}`}
            </Button>
          </Col>
        </Row>
        <Table
          columns={this.getColumns(category)}
          dataSource={list}
          bordered
          size="small"
          rowKey="id"
          loading={loading.effects['m2Protocol/list']}
          pagination={{
            total,
            showSizeChanger: true,
            showQuickJumper: true,
            size: 'small',
            showTotal: t => `Total ${t} items`,
            pageSizeOptions: ['10', '30', '50'],
            current: currentPage,
            pageSize,
          }}
          onChange={this.onTableChange}
          onRow={record => {
            return {
              onDoubleClick: () => {
                router.push(`/m2/field/${record.id}`);
              },
            };
          }}
        />
        <Modal
          title={`${editMode ? 'Update' : 'Create'}${this.getCategoryName()}`}
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
