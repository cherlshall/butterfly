import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Drawer, Button, Row, Col, Spin, Icon, Input, Radio, Select, Modal, Popconfirm, Table, message } from 'antd';
import GridContent from '@/components/PageHeaderWrapper/GridContent';
import ResizableTable from "@/mycomponents/ResizableTable";
import styles from './TableOperation.less';
import InsertDialog from './InsertDialog';
import Highlighter from 'react-highlight-words';
import moment from 'moment';

const { Option } = Select;
const { Search } = Input;

const rotateTimeOutId = [];

@connect(({ tableOperation, loading }) => ({
  tableOperation,
  loading,
}))
class TableOperation extends React.Component {

  state = {
    currentTableName: '',
    drawerVisible: false,
    filterTable: '',
    showMode: 1,
    lastRowKey: '',
    pageSize: 10,
    insertDialogVisible: false,
    columns: [],
    deleteRowKey: new Set(),
    deleteColId: new Set(),
    timeMode: true,
    rotate: 0,
    editColIndex: -1,
    recoverIndex: new Set(),
  }

  componentDidMount() {
    const { tableName } = this.props.match.params;
    if (tableName) {
      this.listTableName(() => this.findFirstPage(tableName));
    } else {
      this.listTableName(this.findFirstPage);
    }
  }

  findFirstPage = (tableName) => {
    this.setState({
      lastRowKey: '',
    }, () => this.findByPage(tableName, false))
  }

  findByPage = (tableName, removeFirst) => {
    const { dispatch } = this.props;
    const { lastRowKey, pageSize } = this.state;
    dispatch({
      type: 'tableOperation/findByPage',
      payload: {
        tableName,
        pageSize,
        rowKey: lastRowKey,
        removeFirst: removeFirst === undefined ? true : removeFirst,
      },
      callback: (lastRowKey, familyAndQualifiers) => {
        const newState = {
          currentTableName: tableName,
        }
        if (lastRowKey !== undefined) {
          newState.lastRowKey = lastRowKey;
        }
        this.setState(newState)
        this.generaterColumns(familyAndQualifiers)
      },
    });
  }

  deleteRow = rowKey => {
    const { currentTableName } = this.state;
    const { dispatch } = this.props;
    this.setState({
      deleteRowKey: this.state.deleteRowKey.add(rowKey),
    })
    dispatch({
      type: 'tableOperation/deleteRow',
      payload: {
        tableName: currentTableName,
        rowKey,
      },
      callback: () => {
        const deleteRowKey = this.state.deleteRowKey;
        deleteRowKey.delete(rowKey);
        this.setState({
          deleteRowKey,
        })
      },
    });
  }

  deleteCol = record => {
    const { currentTableName } = this.state;
    const { dispatch } = this.props;
    const colId = this.getColId(record);
    this.setState({
      deleteColId: this.state.deleteColId.add(colId),
    })
    dispatch({
      type: 'tableOperation/deleteCol',
      payload: {
        tableName: currentTableName,
        ...record,
      },
      callback: () => {
        const deleteColId = this.state.deleteColId;
        deleteColId.delete(colId);
        this.setState({
          deleteColId,
        })
      },
    });
  }

  generaterColumns = familyAndQualifiers => {
    const columns = [
      {
        title: 'rowKey',
        dataIndex: 'rowKey',
        width: 120,
      }
    ];
    for (let i = 0; i < familyAndQualifiers.length; i++) {
      const faq = familyAndQualifiers[i];
      const column = {title: faq.family, children: []};
      const quas = faq.qualifiers;
      for (let j = 0; j < quas.length; j++) {
        const qua = quas[j];
        column.children.push({
          title: qua,
          dataIndex: faq.family + "." + qua,
          width: 120,
        });
      }
      columns.push(column);
    }
    columns.push({
      title: 'Action',
      key: 'action',
      render: (text, record) => {
        const deleting = this.state.deleteRowKey.has(record.rowKey);
        const disabled = record.deleted || deleting;
        const popParams = {};
        if (disabled) {
          popParams.visible = false;
        }
        return (
          <Popconfirm
            title="Are you sure delete this row?"
            onConfirm={() => this.deleteRow(record.rowKey)}
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
      )},
    })
    this.setState({
      columns,
    })
  }

  findNextPage = () => {
    this.findByPage(this.state.currentTableName, true)
  }

  listTableName = (callback) => {
    const { dispatch } = this.props;
    dispatch({
      type: 'tableOperation/listTableName',
      callback,
    });
  }

  showDrawer = () => {
    this.setState({
      drawerVisible: true,
    });
  };

  onClose = () => {
    this.setState({
      drawerVisible: false,
    });
  };

  changePageSize = (value) => {
    this.setState({
      pageSize: value,
    }, () => this.findFirstPage(this.state.currentTableName))
  }

  changeInsertDialogVisible = (visible) => {
    this.setState({
      insertDialogVisible: visible,
    });
  };

  insertOver = (rowKey) => {
    this.changeInsertDialogVisible(false);
    this.setState({
      lastRowKey: rowKey,
    }, () => this.findByPage(this.state.currentTableName, false))
  }

  findWithStartRowKey = value => {
    if (!value) {
      message.warn("rowKey cannot be empty");
      return;
    }
    this.setState({
      lastRowKey: value,
    }, () => this.findByPage(this.state.currentTableName, false))
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
    render: (text, record) => (
      <Highlighter
        highlightStyle={{ backgroundColor: '#ffc069', padding: 0 }}
        searchWords={[this.state.searchText]}
        autoEscape
        textToHighlight={text.toString()}
      />
    ),
      
  });

  handleSearch = (selectedKeys, confirm) => {
    confirm();
    this.setState({ searchText: selectedKeys[0] });
  };

  handleReset = clearFilters => {
    clearFilters();
    this.setState({ searchText: '' });
  };

  changeTimeMode = () => {
    if (rotateTimeOutId) {
      for (let i = 0; i < rotateTimeOutId.length; i++) {
        clearTimeout(rotateTimeOutId[i]);
      }
    }
    this.setState({
      timeMode: !this.state.timeMode,
    })
    for (let i = 1; i < 6; i++) {
      rotateTimeOutId[i] = setTimeout(() => {
        const rotate = 36 * i;
        this.setState({
          rotate,
        })
      }, 100 * i)
    }
  }

  changeEditIndex = (index) => {
    if (this.cancleEditTimeout) {
      clearTimeout(this.cancleEditTimeout);
    }
    setTimeout(() => {
      const ref = this[`search_${index}`];
      if (ref) {
        this[`search_${index}`].focus();
      }
    }, 100)
    this.setState({
      editColIndex: index,
    })
  }

  updateValue = (record, value, index) => {
    if (this.cancleEditTimeout) {
      clearTimeout(this.cancleEditTimeout);
    }
    this.insertRow(record, value, (rowKey) => {
      this.setState({
        editColIndex: -1,
      })
      this.insertOver(rowKey)
    })
  }

  onBlurValueInput = () => {
    this.cancleEditTimeout = setTimeout(() => {
      this.setState({
        editColIndex: -1,
      })
    }, 200)
  }

  insertRow = (record, value, callback) => {
    const { rowKey, family, qualifier } = record;
    const { dispatch } = this.props;
    dispatch({
      type: 'tableOperation/insertRow',
      payload: {
        tableName: this.state.currentTableName,
        rowKey,
        beans: [{family, qualifier, value}],
      },
      callback: () => callback(rowKey),
    });
  }

  recoverRow = (record, callback) => {
    const { rowKey, family, qualifier, value } = record;
    const { dispatch } = this.props;
    dispatch({
      type: 'tableOperation/recoverRow',
      payload: {
        tableName: this.state.currentTableName,
        rowKey,
        beans: [{family, qualifier, value}],
      },
      callback,
    });
  }

  pressRecover = (record, index) => {
    this.setState({
      recoverIndex: this.state.recoverIndex.add(index),
    })
    this.recoverRow(record, () => {
      this.state.recoverIndex.delete(index);
      this.setState({
        recoverIndex: this.state.recoverIndex,
      })
    })
  }

  columnsCol = [
    {
      title: 'RowKey',
      dataIndex: 'rowKey',
      width: 200,
      ...this.getColumnSearchProps('rowKey'),
    },
    {
      title: 'Family',
      dataIndex: 'family',
      width: 200,
    },
    {
      title: 'Qualifier',
      dataIndex: 'qualifier',
      width: 200,
    },
    {
      title: 'Value',
      dataIndex: 'value',
      width: 200,
      render: (text, record, index) => {
        if (this.state.editColIndex === index) {
          return (
          <Spin spinning={!!this.props.loading.effects['tableOperation/insertRow']}>
            <Search
              defaultValue={text}
              enterButton="Save"
              size="small"
              onSearch={value => this.updateValue(record, value)}
              onBlur={this.onBlurValueInput}
              ref={search => {
                if (search) {
                  this[`search_${index}`] = search
                }
              }}
              id={`search_${index}`}
            />
          </Spin>)
        } else {
          return <div onClick={() => this.changeEditIndex(index)}>{text}</div>;
        }
      }
    },
    {
      title: () => (<div>
        <span>Timestamp</span>
        <Icon 
          type="swap" 
          onClick={this.changeTimeMode}
          style={{color: '#1890FF', marginLeft: 8}}
          rotate={this.state.rotate}
        />
      </div>),
      dataIndex: 'timestamp',
      width: 200,
      render: text => this.state.timeMode ? moment(text).format("YYYY-MM-DD HH:mm:ss") : text,
    },
    {
      title: 'Action',
      key: 'action',
      render: (text, record, index) => {
        const deleting = this.state.deleteColId.has(this.getColId(record));
        const disabled = record.deleted || deleting;
        const popParams = {};
        if (disabled) {
          popParams.visible = false;
        }
        const recovering = this.state.recoverIndex.has(index);
        return (
          <div>
            {!record.deleted && <Button 
              style={{cursor: disabled ? "not-allowed" : "pointer", marginRight: 8}} 
              type="primary" 
              size="small"
              disabled={disabled}
              icon={'edit'}
              onClick={() => this.changeEditIndex(index)}
            >
              {'Edit'}
            </Button>}
            {record.deleted && <Button 
              style={{cursor: "pointer", marginRight: 8}} 
              type="primary" 
              size="small"
              icon={recovering ? 'loading' : 'redo'}
              onClick={() => {this.pressRecover(record, index)}}
              disabled={recovering}
            >
              {'Recover'}
            </Button>}
            {!record.deleted && <Popconfirm
              title="Are you sure delete this row?"
              onConfirm={() => this.deleteCol(record)}
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
            </Popconfirm>}
          </div>
      )},
    }
  ]

  getColId = (record) => record.rowKey + "." + record.family + "." + record.qualifier + "." + record.value + "." + record.timestamp;

  render() {
    const { tableOperation, loading } = this.props;
    const { dataSource, dataSourceCol, tableNames } = tableOperation;
    const { columns, currentTableName, filterTable, drawerVisible, lastRowKey, pageSize, showMode,
      insertDialogVisible } = this.state;

      return (
      <GridContent>
        <Row gutter={24} style={{marginBottom: 12}}>
          <Col span={12}>
            {currentTableName && 
            <div>
              <Button 
                type="primary"
                onClick={() => this.changeInsertDialogVisible(true)}
                style={{marginRight: 12}}
              >
                {`Insert To ${currentTableName}`}
              </Button>
              <Search
                placeholder="input start rowKey"
                enterButton="Go!"
                onSearch={this.findWithStartRowKey}
                style={{display: 'inline-block', width: 220, marginRight: 12}}
              />
            </div>
            }
          </Col>
          <Col span={12} style={{textAlign: "right"}}>
            <Radio.Group 
                value={showMode} 
                onChange={(e) => this.setState({showMode: e.target.value})}
                buttonStyle="solid"
                style={{marginRight: 12}}
              >
                <Radio.Button value={1}>ROW</Radio.Button>
                <Radio.Button value={2}>COL</Radio.Button>
            </Radio.Group>
            <Button type="primary" icon="left" onClick={this.showDrawer}>ALL TABLE</Button>
          </Col>
        </Row>
        {showMode === 1 ? 
          <ResizableTable
            columns={columns}
            dataSource={dataSource}
            rowKey="rowKey"
            bordered
            size="middle"
            loading={(!currentTableName && loading.effects["tableOperation/listTableName"]) || 
              loading.effects["tableOperation/findByPage"]}
            pagination={false}
          /> : 
          <ResizableTable
            columns={this.columnsCol}
            dataSource={dataSourceCol}
            rowKey={this.getColId}
            bordered
            size="middle"
            loading={(!currentTableName && loading.effects["tableOperation/listTableName"]) || 
              loading.effects["tableOperation/findByPage"]}
            pagination={{
              total: dataSourceCol.length,
              showSizeChanger: true,
              showQuickJumper: true,
              size: 'middle',
              showTotal: (total) => `Total ${total} columns`,
              pageSizeOptions: ["10", "30", "50"],
            }}
            editColIndex={this.state.editColIndex}
          />
        }
        <div style={{width: '100%', textAlign: 'right', marginTop: 12}}>
          <Select style={{ width: 100 }} value={pageSize} onChange={this.changePageSize}>
            <Option value={10}>10 rows</Option>
            <Option value={30}>30 rows</Option>
            <Option value={50}>50 rows</Option>
          </Select>
          {lastRowKey !== '' && 
            <Button type='primary' style={{marginLeft: 12}} onClick={() => this.findFirstPage(currentTableName)}>
              FIRST
            </Button>}
          {dataSource.length >= pageSize && 
            <Button type='primary' style={{marginLeft: 12}} onClick={this.findNextPage}>
              NEXT
            </Button>}
        </div>
        <Drawer
          title={
            <div>
              <span style={{display: "inline-block", width: "70%"}}>ALL TABLE</span>
              <span style={{display: "inline-block", width: "30%", textAlign: "right"}}>
                <Icon type="reload" style={{cursor: "pointer", color: "#096dd9"}} onClick={this.listTableName} />
              </span>
              <Input 
                style={{marginTop: 12}} 
                allowClear 
                onChange={(e) => {this.setState({filterTable: e.target.value})}}
                placeholder="For Search"
                prefix={<Icon type="search" style={{ color: 'rgba(0,0,0,.25)'}} />}
               />
            </div>
          }
          placement="right"
          closable={false}
          onClose={this.onClose}
          visible={drawerVisible}
        >
          <Spin spinning={!!loading.effects["tableOperation/listTableName"]}>
            {tableNames.filter((tableName) => {
              if (tableName.indexOf(filterTable) !== -1) {
                return true;
              } else {
                return false;
              }
            }).map((tableName) => (
              <p 
                key={tableName}
                className={currentTableName === tableName ? styles.table_name_select : styles.table_name}
                onClick={() => this.findFirstPage(tableName)}
              >
                {tableName}
              </p>
            ))}
          </Spin>
        </Drawer>
        <Modal
          // destroyOnClose={true}
          title={`Insert To ${currentTableName}`}
          visible={insertDialogVisible}
          onCancel={() => this.changeInsertDialogVisible(false)}
          footer={null}
        >
          <InsertDialog insertOver={this.insertOver} tableName={currentTableName} />
        </Modal>
      </GridContent>
    )
  }
}

export default TableOperation;