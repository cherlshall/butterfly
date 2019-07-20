import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Drawer, Button, Row, Col, Spin, Icon, Input, Radio, Select, Modal, Popconfirm, Table } from 'antd';
import GridContent from '@/components/PageHeaderWrapper/GridContent';
import ResizableTable from "@/mycomponents/ResizableTable";
import styles from './TableOperation.less';
import InsertDialog from './InsertDialog';

const { Option } = Select;

@connect(({ tableOperation, loading }) => ({
  tableOperation,
  loading,
}))
class TableOperation extends PureComponent {

  state = {
    currentTableName: '',
    drawerVisible: false,
    filterTable: '',
    showMode: 1,
    lastRowKey: '',
    pageSize: 10,
    onFirstPage: true,
    insertDialogVisible: false,
    columns: [],
    deleteRowKey: new Set([]),
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
    }, () => this.findByPage(tableName, true))
  }

  findByPage = (tableName, onFirstPage, removeFirst) => {
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
        if (onFirstPage !== undefined) {
          newState.onFirstPage = onFirstPage;
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
    this.findByPage(this.state.currentTableName, false)
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
    }, () => this.findByPage(this.state.currentTableName, false, false))
  }

  render() {
    const { tableOperation, loading } = this.props;
    const { dataSource, tableNames } = tableOperation;
    const { columns, currentTableName, filterTable, drawerVisible, lastRowKey, pageSize, onFirstPage, 
      insertDialogVisible } = this.state;

    return (
      <GridContent>
        <Row gutter={24} style={{marginBottom: 12}}>
          <Col span={12}>
            {currentTableName && 
              <Button 
                type="primary"
                onClick={() => this.changeInsertDialogVisible(true)}
              >
                {`Insert To ${currentTableName}`}
              </Button>
            }
          </Col>
          <Col span={12} style={{textAlign: "right"}}>
            <Radio.Group 
                value={this.state.showMode} 
                onChange={(e) => this.setState({showMode: e.target.value}, () => this.findFirstPage(currentTableName))}
                buttonStyle="solid"
                style={{marginRight: 12}}
              >
                <Radio.Button value={1}>ROW</Radio.Button>
                <Radio.Button value={2}>COL</Radio.Button>
            </Radio.Group>
            <Button type="primary" icon="left" onClick={this.showDrawer}>ALL TABLE</Button>
          </Col>
        </Row>
        <ResizableTable
          columns={columns}
          dataSource={dataSource}
          rowKey="rowKey"
          bordered
          size="middle"
          loading={(!currentTableName && loading.effects["tableOperation/listTableName"]) || 
            loading.effects["tableOperation/findByPage"]}
          pagination={false}
        />
        <div style={{width: '100%', textAlign: 'right', marginTop: 12}}>
          <Select style={{ width: 100 }} value={pageSize} onChange={this.changePageSize}>
            <Option value={10}>10 items</Option>
            <Option value={30}>30 items</Option>
            <Option value={50}>50 items</Option>
          </Select>
          {(!onFirstPage) && 
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
