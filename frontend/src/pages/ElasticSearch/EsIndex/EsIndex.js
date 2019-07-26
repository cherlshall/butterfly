import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Table, Button, Row, Col, Card, Icon, Input, Radio, Tag, Popconfirm, Progress, message, Tooltip } from 'antd';
import GridContent from '@/components/PageHeaderWrapper/GridContent';
import Highlighter from 'react-highlight-words';
import router from 'umi/router';
import Link from 'umi/link';
import { Pie, WaterWave, Gauge, TagCloud } from '@/components/Charts';
import ActiveChart from '@/mycomponents/ActiveChart';
import styles from './EsIndex.less';
import CreateDocDialog from './CreateDocDialog';
import ListDrawer from "@/mycomponents/ListDrawer";
import { object } from 'prop-types';
import { splitLongText } from '@/utils/utils';

@connect(({ esIndex, loading }) => ({
  esIndex,
  loading,
}))
class EsIndex extends React.Component {

  state = {
    currentIndexName: '',
    currentPage: 1,
    pageSize: 10,
    createDocVisible: false,
    deleteDocId: new Set(),
    recoverDocId: new Set(),
    drawerVisible: false,
  }

  componentDidMount() {
    const { indexName } = this.props.match.params;
    if (indexName !== undefined) {
      this.changeIndex(indexName);
      this.listIndex();
    } else {
      this.listIndex(this.changeIndex);
    }
  }

  listData = (indexName) => {
    const { dispatch } = this.props;
    const { currentIndexName, currentPage, pageSize } = this.state;
    dispatch({
      type: 'esIndex/listData',
      payload: {
        indexName: indexName || currentIndexName,
        currentPage,
        pageSize,
      },
    })
  }

  listIndex = (callback) => {
    const { dispatch } = this.props;
    dispatch({
      type: 'esIndex/listIndex',
      callback,
    })
  }

  changeIndex = (indexName) => {
    this.setState({
      currentIndexName: indexName,
      currentPage: 1,
      pageSize: 10,
    })
    this.listData(indexName);
    this.properties(indexName);
  }

  properties = (indexName) => {
    const { dispatch } = this.props;
    const { currentIndexName, currentPage, pageSize } = this.state;
    dispatch({
      type: 'esIndex/properties',
      payload: {
        indexName: indexName || currentIndexName,
      },
    })
  }

  propertiesToColumns = properties => {
    const columns = [{
      title: '_id',
      dataIndex: '_id',
      render: text => splitLongText(text),
    }];
    properties.forEach(property => {
      columns.push({
        title: property,
        dataIndex: property,
        render: text => splitLongText(text),
      })
    })
    columns.push({
      title: 'Option',
      key: 'option',
      width: 120,
      fixed: 'right',
      render: (text, record) => {
        const deleting = this.state.deleteDocId.has(record._id);
        const recovering = this.state.recoverDocId.has(record._id);
        const popParams = {};
        if (deleting) {
          popParams.visible = false;
        }
        return (
          <div>
            {record._deleted && <Button 
              style={{cursor: "pointer", marginRight: 8}} 
              type="primary" 
              size="small"
              icon={'redo'}
              onClick={() => {this.pressRecover(record)}}
              disabled={recovering}
              loading={recovering}
            >
              {'Recover'}
            </Button>}
            {!record._deleted && <Popconfirm
              title="Are you sure delete this doc?"
              onConfirm={() => {this.deleteDoc(record._id)}}
              okText="Yes"
              cancelText="No"
              {...popParams}
            >
              <Button 
                style={{cursor: "pointer"}} 
                type="danger" 
                size="small"
                icon={'delete'}
                disabled={deleting}
                loading={deleting}
              >
                {"Delete"}
              </Button>
            </Popconfirm>}
          </div>
        )
      }
    })
    return columns;
  }

  deleteDoc = id => {
    const { dispatch } = this.props;
    this.setState({
      deleteDocId: this.state.deleteDocId.add(id),
    })
    dispatch({
      type: 'esIndex/deleteDoc',
      payload: {
        indexName: this.state.currentIndexName,
        id,
      },
      callback: () => {
        this.state.deleteDocId.delete(id);
        this.setState({
          deleteDocId: this.state.deleteDocId,
        })
        // this.health();
      },
    });
  }

  pressRecover = (record) => {
    this.setState({
      recoverDocId: this.state.recoverDocId.add(record._id),
    })
    this.recoverDoc(record, () => {
      this.state.recoverDocId.delete(record._id);
      this.setState({
        recoverDocId: this.state.recoverDocId,
      })
    })
  }

  recoverDoc = (record, callback) => {
    const { _deleted, ...bean } = record;
    const { dispatch } = this.props;
    dispatch({
      type: 'esIndex/recoverDoc',
      payload: {
        indexName: this.state.currentIndexName,
        bean,
      },
      callback,
    });
  }

  changeCreateDocVisible = (visible) => {
    this.setState({
      createDocVisible: visible,
    });
  };

  changeDrawerVisible = (visible) => {
    this.setState({
      drawerVisible: visible,
    });
  };

  onTableChange = (pagination, filters, sorter) => {
    const { current, pageSize } = pagination;
    if (current * pageSize > 10000) {
      message.error("page too large")
      return;
    }
    this.setState({
      currentPage: current,
      pageSize,
    }, this.listData)
  }

  render() {
    const { esIndex, loading } = this.props;
    const { properties, dataSource, total, indices } = esIndex;
    const { currentIndexName, createDocVisible, currentPage, pageSize } = this.state;
    
    const columns = this.propertiesToColumns(properties);

    return (
      <GridContent>
        <Row style={{marginBottom: 8}}>
          <Col span={12}>
            <Button 
              type="primary"
              onClick={() => this.listData()}
              style={{marginRight: 12}}
              icon='reload'
              loading={loading.effects["esIndex/listData"]}
            >
              {currentIndexName}
            </Button>
            <Button
              type='primary'
              onClick={() => this.changeCreateDocVisible(true)}
            >
              Create Document
            </Button>
          </Col>
          <Col span={12} style={{textAlign: "right"}}>
            <Button type="primary" icon="left" onClick={() => this.changeDrawerVisible(true)}>ALL INDEX</Button>
          </Col>
        </Row>
        <Table
          columns={columns}
          dataSource={dataSource}
          bordered
          size="middle"
          rowKey="_id"
          loading={loading.effects["esIndex/listData"]}
          pagination={{
            total: total,
            showSizeChanger: true,
            showQuickJumper: true,
            size: 'middle',
            showTotal: (total) => `Total ${total} items`,
            pageSizeOptions: ["10", "30", "50"],
            current: currentPage,
            pageSize: pageSize,
          }}
          onChange={this.onTableChange}
          scroll={{x: columns.length > 5 ? 'max-content' : false}}
        />
        <CreateDocDialog
          visible={createDocVisible} 
          closeDialog={() => this.changeCreateDocVisible(false)}
          // createOver={() => this.listData()}
          indexName={currentIndexName}
          properties={properties}
        />
        <ListDrawer 
          title='ALL INDEX'
          loading={!!loading.effects["esIndex/listTableName"]}
          onReload={this.listIndex}
          visible={this.state.drawerVisible}
          onClose={() => this.changeDrawerVisible(false)}
          dataSource={indices}
          selected={currentIndexName}
          onSelect={this.changeIndex}
        />
      </GridContent>
    )
  }
}

export default EsIndex;
