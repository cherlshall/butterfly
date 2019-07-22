import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Table, Button, Row, Col, Switch, Icon, Input, Radio, Tag, Popconfirm, Modal, message } from 'antd';
import GridContent from '@/components/PageHeaderWrapper/GridContent';
import Highlighter from 'react-highlight-words';
import styles from './AdminOperation.less';
import CreateTableDialog from './CreateTableDialog';
import router from 'umi/router';
import Link from 'umi/link';
import EditableTags from '@/mycomponents/EditableTags';

// 跳转
// router.push('/hbase/tableOperation/:tableName');

@connect(({ adminOperation, loading }) => ({
  adminOperation,
  loading,
}))
class AdminOperation extends PureComponent {

  state = {
    changeDisableTableName: new Set(),
    deleteTableName: new Set(),
    remakeTableName: new Set(),
    createDialogVisible: false,
    editTableName: '',
    editingTableName: new Set(),
  }

  componentDidMount() {
    this.detail();
  }

  changeCreateDialogVisible = (visible) => {
    this.setState({
      createDialogVisible: visible,
    });
  };

  detail = () => {
    const { dispatch } = this.props;
    dispatch({
      type: 'adminOperation/detail',
    });
  }

  disable = tableName => {
    const { dispatch } = this.props;
    this.setState({
      changeDisableTableName: this.state.changeDisableTableName.add(tableName),
    })
    dispatch({
      type: 'adminOperation/disable',
      payload: {
        tableName,
      },
      callback: () => {
        const changeDisableTableName = this.state.changeDisableTableName;
        changeDisableTableName.delete(tableName);
        this.setState({
          changeDisableTableName,
        })
      },
    });
  }

  enable = tableName => {
    const { dispatch } = this.props;
    this.setState({
      changeDisableTableName: this.state.changeDisableTableName.add(tableName),
    })
    dispatch({
      type: 'adminOperation/enable',
      payload: {
        tableName,
      },
      callback: () => {
        const changeDisableTableName = this.state.changeDisableTableName;
        changeDisableTableName.delete(tableName);
        this.setState({
          changeDisableTableName,
        })
      },
    });
  }

  deleteTable = tableName => {
    const { dispatch } = this.props;
    this.setState({
      deleteTableName: this.state.deleteTableName.add(tableName),
    })
    dispatch({
      type: 'adminOperation/del',
      payload: {
        tableName,
      },
      callback: () => {
        const deleteTableName = this.state.deleteTableName;
        deleteTableName.delete(tableName);
        this.setState({
          deleteTableName,
        })
      },
    });
  }

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
          onPressEnter={() => this.handleSearch(selectedKeys, confirm)}
          style={{ width: 188, marginBottom: 8, display: 'block' }}
        />
        <Button
          type="primary"
          onClick={() => this.handleSearch(selectedKeys, confirm)}
          icon="search"
          size="small"
          style={{ width: 90, marginRight: 8 }}
        >
          Search
        </Button>
        <Button onClick={() => this.handleReset(clearFilters)} size="small" style={{ width: 90 }}>
          Reset
        </Button>
      </div>
    ),
    filterIcon: filtered => (
      <Icon type="search" style={{ color: filtered ? '#1890ff' : undefined }} />
    ),
    onFilter: (value, record) =>
      record[dataIndex]
        .toString()
        .toLowerCase()
        .includes(value.toLowerCase()),
    onFilterDropdownVisibleChange: visible => {
      if (visible) {
        setTimeout(() => this.searchInput.select());
      }
    },
    render: (text, record) => {
      if (record.disable || record.deleted || this.state.deleteTableName.has(record.tableName)) {
        return (
          <Highlighter
            highlightStyle={{ backgroundColor: '#ffc069', padding: 0 }}
            searchWords={[this.state.searchText]}
            autoEscape
            textToHighlight={text.toString()}
            onClick={() => {message.error(record.disable ? 'table is disabled' : 
              record.deleted ? 'table has been deleted' : 'table is being deleted')}}
            style={{cursor: "not-allowed", color: "#A9A9A9"}}
          />
        )
      } else {
        return (
          <Link to={`/hbase/tableOperation/${text}`}>
            <Highlighter
              highlightStyle={{ backgroundColor: '#ffc069', padding: 0 }}
              searchWords={[this.state.searchText]}
              autoEscape
              textToHighlight={text.toString()}
            />
          </Link>
        )
      }
    },
      
  });

  handleSearch = (selectedKeys, confirm) => {
    confirm();
    this.setState({ searchText: selectedKeys[0] });
  };

  handleReset = clearFilters => {
    clearFilters();
    this.setState({ searchText: '' });
  };

  remake = (tableName, families) => {
    const { dispatch } = this.props;
    dispatch({
      type: 'adminOperation/remake',
      payload: {
        tableName,
        families,
        remakeTableName: this.state.remakeTableName.add(tableName),
      },
      callback: () => {
        this.state.remakeTableName.delete(tableName);
        this.setState({
          remakeTableName: this.state.remakeTableName,
        })
      },
    });
  }

  createOver = () => {
    this.changeCreateDialogVisible(false);
    this.detail();
  }

  saveTags = (deleteValue, additionTagsValue) => {
    const { dispatch } = this.props;
    const { editTableName } = this.state;
    this.setState({
      editingTableName: this.state.editingTableName.add(editTableName),
    })
    dispatch({
      type: 'adminOperation/changeFamily',
      payload: {
        tableName: editTableName,
        remove: deleteValue,
        addition: additionTagsValue,
      },
      callback: () => {
        this.state.editingTableName.delete(editTableName);
        this.setState({
          editTableName: '',
          editingTableName: this.state.editingTableName,
        })
        this.detail();
      }
    });
  }

  columns = [
    {
      title: 'Table Name',
      dataIndex: 'tableName',
      key: 'tableName',
      width: 160,
      ...this.getColumnSearchProps('tableName'),
    },
    {
      title: 'Replica',
      dataIndex: 'regionReplication',
      key: 'regionReplication',
      width: 80,
    },
    {
      title: 'Read Only',
      dataIndex: 'readOnly',
      key: 'readOnly',
      width: 80,
      render: text => <span>{text ? "yes" : "no"}</span>,
    },
    {
      title: 'Enabled',
      dataIndex: 'disable',
      key: 'disable',
      width: 80,
      render: (text, record, index) => (
        <Switch
          checkedChildren={<Icon type="check" />}
          unCheckedChildren={<Icon type="close" />}
          checked={!text}
          loading={this.state.changeDisableTableName.has(record.tableName)}
          disabled={record.deleted || this.state.deleteTableName.has(record.tableName)}
          onClick={text ? () => this.enable(record.tableName) : () => this.disable(record.tableName)}
        />
      ),
    },
    {
      title: 'Families',
      dataIndex: 'families',
      key: 'families',
      width: 380,
      render: (text, record, index) => this.state.editTableName === record.tableName ? (
        <EditableTags initValues={text} save={this.saveTags} saving={this.state.editingTableName.has(record.tableName)} />
      ) : (
        <span>
          {text.map(family => {
            return (
              <Tag color='geekblue' key={family}>
                {family}
              </Tag>
            );
          })}
        </span>
      ),
    },
    {
      title: 'Action',
      key: 'action',
      width: 160,
      render: (text, record) => {
        const deleting = this.state.deleteTableName.has(record.tableName);
        const remaking = this.state.remakeTableName.has(record.tableName);
        const disabled = record.deleted || deleting;
        const editing = this.state.editTableName === record.tableName;
        const popParams = {};
        if (disabled) {
          popParams.visible = false;
        }
        return record.deleted ? (
          <Button 
            style={{cursor: "pointer"}} 
            type="primary" 
            size="small"
            disabled={remaking}
            icon={remaking ? 'loading' : 'redo'}
            onClick={() => this.remake(record.tableName, record.families)}
            style={{cursor: remaking ? 'not-allowed' : 'pointer'}}
          >
            Remake
          </Button>) : (
          <div>
            <Button 
              style={{cursor: "pointer", marginRight: 8}} 
              type={editing ? "default" : 'primary'}
              size="small"
              disabled={disabled}
              icon={editing ? 'close-circle' : 'edit'}
              onClick={() => this.setState({editTableName: editing ? '' : record.tableName})}
            >
              {editing ? "Cancel" : "Edit"}
            </Button>
            <Popconfirm
              title="Are you sure delete this table?"
              onConfirm={() => this.deleteTable(record.tableName)}
              okText="Yes"
              cancelText="No"
              disabled={disabled}
              {...popParams}
            >
              <Button 
                style={{cursor: "pointer"}} 
                type="danger" 
                size="small"
                disabled={disabled}
                icon={deleting ? 'loading' : record.deleted ? 'close-circle' : 'delete'}
              >
                {record.deleted ? "Deleted" : "Delete"}
              </Button>
            </Popconfirm>
          </div>
          
        )},
    }
  ]

  render() {
    const { adminOperation, loading } = this.props;
    const { dataSource } = adminOperation;
    const { createDialogVisible } = this.state;

    return (
      <GridContent>
        <Row gutter={24} style={{marginBottom: 12}}>
          <Col span={24}>
            <Button type="primary" onClick={() => this.changeCreateDialogVisible(true)}>Create Table</Button>
          </Col>
        </Row>
        <Table
          columns={this.columns}
          dataSource={dataSource}
          bordered
          size="middle"
          rowKey="tableName"
          loading={!!loading.effects["adminOperation/detail"]}
          pagination={{
            total: dataSource.length,
            showSizeChanger: true,
            showQuickJumper: true,
            size: 'middle',
            showTotal: (total) => `Total ${total} items`,
            pageSizeOptions: ["10", "30", "50"],
          }}
        />
        <Modal
          title="Create Table"
          visible={this.state.createDialogVisible}
          onCancel={() => this.changeCreateDialogVisible(false)}
          footer={null}
        >
          <CreateTableDialog createOver={this.createOver} />
        </Modal>
      </GridContent>
    )
  }
}

export default AdminOperation;
