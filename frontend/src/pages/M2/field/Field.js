import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Table, Button, Row, Col, Switch, Icon, Input, Popconfirm, Modal } from 'antd';
import GridContent from '@/components/PageHeaderWrapper/GridContent';
import Highlighter from 'react-highlight-words';
import Link from 'umi/link';
import router from 'umi/router';
import CreateDialog from './CreateDialog';
import { splitLongText, toHexString, getEveryFirst } from '@/utils/utils';

@connect(({ m2Field, loading }) => ({
  m2Field,
  loading,
}))
class Field extends PureComponent {
  state = {
    createDialogVisible: false,
    editMode: false,
    editRecord: {},
    currentPage: 1,
    pageSize: 10,
    orderName: 'type',
    orderDirection: 'asc',
    deleteFieldId: new Set(),
    changeActiveFieldId: new Set(),
    protocolId: 0,
    protocolEnName: '',
    filters: {},
    searchs: {},
  };

  componentDidMount() {
    this.init();
    window.onhashchange = this.init;
  }

  componentWillUnmount() {
    window.onhashchange = undefined;
  }

  init = () => {
    const { match } = this.props;
    const { protocolId, protocolEnName } = match.params;
    this.setState(
      {
        protocolId,
        protocolEnName,
      },
      this.list
    );
    this.findProtocolById(protocolId);
  };

  findProtocolById = protocolId => {
    const { dispatch } = this.props;
    dispatch({
      type: 'm2Field/findProtocolById',
      payload: {
        id: protocolId,
      },
    });
  };

  list = () => {
    const { dispatch } = this.props;
    const {
      protocolId,
      currentPage,
      pageSize,
      orderName,
      orderDirection,
      filters,
      searchs,
    } = this.state;
    dispatch({
      type: 'm2Field/list',
      payload: {
        protocolId,
        currentPage,
        pageSize,
        orderName,
        orderDirection,
        ...filters,
        ...searchs,
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
        filters: getEveryFirst(filters),
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
    const value = selectedKeys[0];
    this.setState({
      searchText: selectedKeys[0],
      searchedColumn: dataIndex,
    });
    const { searchs } = this.state;
    searchs[dataIndex] = value;
    this.setState(
      {
        ...searchs,
      },
      this.list
    );
  };

  handleReset = (clearFilters, dataIndex) => {
    clearFilters();
    this.setState({ searchText: '' });
    const { searchs } = this.state;
    searchs[dataIndex] = undefined;
    this.setState(
      {
        ...searchs,
      },
      this.list
    );
  };

  goLink = url => {
    router.push(url);
    this.init();
  };

  getColumns = () => {
    const { m2Field } = this.props;
    const { category } = m2Field.protocol;
    let columns = [];
    if (category !== 3) {
      columns.push({
        title: 'TYPE',
        dataIndex: 'type',
        key: 'type',
        width: 100,
        fixed: 'left',
        sorter: true,
        render: text => toHexString(text, 8),
      });
    }
    columns = columns.concat([
      {
        title: 'Name(CN)',
        dataIndex: 'cnName',
        key: 'cnName',
        width: 120,
        ...this.getColumnSearchProps('cnName'),
      },
      {
        title: 'Name(EN)',
        dataIndex: 'enName',
        key: 'enName',
        width: 120,
        ...this.getColumnSearchProps('enName'),
      },
    ]);
    columns.push({
      title: 'Active',
      dataIndex: 'active',
      key: 'active',
      width: 80,
      render: (text, record, index) => {
        const { changeActiveFieldId, deleteFieldId } = this.state;
        return (
          <Switch
            checkedChildren={<Icon type="check" />}
            unCheckedChildren={<Icon type="close" />}
            checked={text === 1}
            loading={changeActiveFieldId.has(record.id)}
            disabled={record.deleted || deleteFieldId.has(record.id)}
            onClick={() => this.changeActive(text === 1 ? 2 : 1, record.id)}
          />
        );
      },
    });
    if (category !== 3) {
      columns.push({
        title: 'Field Count',
        dataIndex: 'fieldCount',
        key: 'fieldCount',
        width: 100,
      });
    }
    columns = columns.concat([
      {
        title: 'Value Type',
        dataIndex: 'valueType',
        key: 'valueType',
        width: 120,
        filters: [
          { text: 'string', value: 'string' },
          { text: 'int', value: 'int' },
          { text: 'long', value: 'long' },
          { text: 'byte', value: 'byte' },
          { text: 'tlv', value: 'tlv' },
          { text: 'extractor', value: 'extractor' },
        ],
        filterMultiple: false,
      },
      {
        title: 'Size',
        dataIndex: 'size',
        key: 'size',
        width: 60,
      },
    ]);
    if (category !== 3) {
      columns.push({
        title: 'Link',
        dataIndex: 'linkEnName',
        key: 'linkEnName',
        width: 80,
        render: (text, record) => (
          <Link to={`/m2/field/${record.link}/${record.linkEnName}`}>{text}</Link>
        ),
      });
    }
    columns = columns.concat([
      {
        title: 'Wireshark Name',
        dataIndex: 'wiresharkName',
        key: 'wiresharkName',
        width: 160,
        render: text => splitLongText(text, 20),
      },
      {
        title: 'Wireshark Filter Syntax',
        dataIndex: 'wiresharkFilterSyntax',
        key: 'wiresharkFilterSyntax',
        width: 200,
        render: text => splitLongText(text, 20),
      },
      {
        title: 'Example',
        dataIndex: 'example',
        key: 'example',
        width: 120,
        render: text => splitLongText(text, 15),
      },
      {
        title: 'Remark',
        dataIndex: 'remark',
        key: 'remark',
        width: 160,
        render: text => splitLongText(text, 20),
      },
      {
        title: 'Option',
        key: 'option',
        width: 180,
        fixed: 'right',
        render: (text, record) => {
          const { deleteFieldId } = this.state;
          const deleting = deleteFieldId.has(record.id);
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
                title="Are you sure delete this field?"
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
      },
    ]);
    return columns;
  };

  changeActive = (active, id) => {
    const { dispatch } = this.props;
    const { changeActiveFieldId } = this.state;
    this.setState({
      changeActiveFieldId: changeActiveFieldId.add(id),
    });
    dispatch({
      type: 'm2Field/changeActive',
      payload: {
        active,
        id,
      },
      callback: () => {
        changeActiveFieldId.delete(id);
        this.setState({
          changeActiveFieldId,
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
    const { deleteFieldId } = this.state;
    this.setState({
      deleteFieldId: deleteFieldId.add(id),
    });
    dispatch({
      type: 'm2Field/deleteById',
      payload: {
        id,
      },
      callback: () => {
        deleteFieldId.delete(id);
        this.setState({
          deleteFieldId,
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

  listFirstPage = () => {
    this.setState(
      {
        currentPage: 1,
        pageSize: 10,
      },
      this.list
    );
  };

  render() {
    const { m2Field, loading } = this.props;
    const { list, total, protocol } = m2Field;
    const {
      protocolId,
      currentPage,
      pageSize,
      createDialogVisible,
      editRecord,
      editMode,
      protocolEnName,
    } = this.state;

    return (
      <GridContent>
        <Row gutter={24} style={{ marginBottom: 12 }}>
          <Col span={24}>
            <Button
              type="primary"
              onClick={this.listFirstPage}
              style={{ marginRight: 12 }}
              icon="reload"
            >
              {protocolEnName}
            </Button>
            <Button type="primary" onClick={() => this.create()}>
              Create Field
            </Button>
          </Col>
        </Row>
        <Table
          columns={this.getColumns()}
          dataSource={list}
          bordered
          size="middle"
          rowKey="tableName"
          loading={loading.effects['m2Field/list']}
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
          scroll={{ x: 'max-content' }}
        />
        <Modal
          title="Create Field"
          visible={createDialogVisible}
          onCancel={() => this.changeCreateDialogVisible(false)}
          footer={null}
          width={680}
          style={{ top: 10 }}
          destroyOnClose
        >
          <CreateDialog
            createOver={this.createOver}
            record={editRecord}
            editMode={editMode}
            protocolId={protocolId}
            category={protocol.category}
          />
        </Modal>
      </GridContent>
    );
  }
}

export default Field;
