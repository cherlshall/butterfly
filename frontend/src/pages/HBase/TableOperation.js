import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Drawer, Button, Row, Col, Spin, Icon, Input, Radio } from 'antd';
import styles from './TableOperation.less';
import GridContent from '@/components/PageHeaderWrapper/GridContent';
import ResizableTable from "@/mycomponents/ResizableTable";

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
  }

  componentDidMount() {
    this.listTableName(this.findFirstPage);
  }

  findFirstPage = (tableName) => {
    this.setState({
      lastRowKey: '',
      currentTableName: tableName,
    }, () => this.findByPage(tableName, true))
  }

  findByPage = (tableName, onFirstPage) => {
    const { dispatch } = this.props;
    const { lastRowKey, pageSize } = this.state;
    dispatch({
      type: 'tableOperation/findByPage',
      payload: {
        tableName,
        pageSize,
        rowKey: lastRowKey,
      },
      callback: (lastRowKey) => {
        const newState = {
          currentTableName: tableName,
          lastRowKey,
        }
        if (onFirstPage !== undefined) {
          newState.onFirstPage = onFirstPage;
        }
        this.setState(newState)
      },
    });
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

  render() {

    const { tableOperation, loading } = this.props;
    const { columns, dataSource, tableNames } = tableOperation;
    const { currentTableName, filterTable, drawerVisible, lastRowKey, pageSize, onFirstPage } = this.state;

    return (
      <GridContent>
        <Row gutter={24} style={{marginBottom: 12}}>
          <Col span={12}>{currentTableName && <Button type="primary">{currentTableName}</Button>}</Col>
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
          rowKey="rowkey"
          loading={!!loading.effects["tableOperation/findByPage"]}
          pagination={false}
        />
        <div style={{width: '100%', textAlign: 'right', marginTop: 12}}>
          {(!onFirstPage) && 
            <Button type='primary' onClick={() => this.findFirstPage(currentTableName)}>
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
      </GridContent>
    )
  }
}

export default TableOperation;
