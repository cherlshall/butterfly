import React, { Component } from 'react';
import { connect } from 'dva';
import { Input, Icon, Button, Table, Spin, Tabs } from 'antd';
import GridContent from '@/components/PageHeaderWrapper/GridContent';

const { TabPane } = Tabs;

@connect(({ hdfs, loading }) => ({
  hdfs,
  loading,
}))
class ContentDialog extends Component {

  state = {
    type: '1',
    currentPage: 1,
    pageSize: 10,
  }

  componentDidMount() {
    this.read();
  }

  componentWillUnmount() {
    this.clear();
  }

  read = () => {
    const { dispatch, filePath } = this.props;
    dispatch({
      type: 'hdfs/read',
      payload: {
        path: filePath,
      },
    })
  }

  readJson = () => {
    const { dispatch, filePath } = this.props;
    const { currentPage, pageSize } = this.state;
    dispatch({
      type: 'hdfs/readJson',
      payload: {
        path: filePath,
        currentPage,
        pageSize,
      },
    })
  }

  clear = () => {
    const { dispatch } = this.props;
    dispatch({
      type: 'hdfs/clearContent',
    })
  }

  changeType = (value) => {
    if (!this.readMoreThanOnce && value === '2') {
      this.readMoreThanOnce = true;
      this.readJson();
    }
    this.setState({
      type: value,
    })
  }

  onTableChange = (pagination, filters, sorter) => {
    const { current, pageSize } = pagination;
    if (current * pageSize > 10000) {
      message.error("page too large")
      return;
    }
    this.setState({
      currentPage: current,
      pageSize,
    }, this.readJson)
  }

  render() {
    const { loading, hdfs, filePath } = this.props;
    const { content, contentColumn, contentDataSource, contentTotal } = hdfs;
    const { type, currentPage, pageSize } = this.state;

    return (
      <GridContent>
        <Tabs activeKey={type} onChange={this.changeType}>
          <TabPane tab="String" key="1">
            <Spin spinning={loading.effects['hdfs/read']} style={{ width: '100%', height: '100%' }}>{content}</Spin>
          </TabPane>
          <TabPane tab="Table" key="2">
            <Table
              columns={contentColumn}
              dataSource={contentDataSource}
              bordered
              size="middle"
              rowKey="id"
              loading={loading.effects["hdfs/readJson"]}
              pagination={{
                total: contentTotal,
                showSizeChanger: true,
                showQuickJumper: true,
                size: 'middle',
                showTotal: (total) => `Total ${total} items`,
                pageSizeOptions: ["10", "30", "50"],
                current: currentPage,
                pageSize: pageSize,
              }}
              onChange={this.onTableChange}
              scroll={{x: contentColumn.length > 5 ? 'max-content' : false}}
            />
          </TabPane>
        </Tabs>
      </GridContent>
    );
  }
}

export default ContentDialog;
