import React, { Component } from 'react';
import { connect } from 'dva';
import { Row, Col, Modal, Icon, Button, Table, Spin, Tabs, Skeleton, Slider } from 'antd';
import GridContent from '@/components/PageHeaderWrapper/GridContent';
// import Json2Html from 'json-pretty-html';
import JSONTree from 'react-json-tree';
import styles from './Hdfs.less';
import ReactJson from 'react-json-view';

const { TabPane } = Tabs;

let textId = 0;
let tableId = 0;
let jsonId = 0;
@connect(({ hdfs, loading }) => ({
  hdfs,
  loading,
}))
class ContentDialog extends Component {

  state = {
    type: '1',
    currentPage: 1,
    pageSize: 10,
    startPoint: 0,
    endPoint: 10,
    jsonLine: '',
    jsonDialogVisible: false,
    textLineTotal: 10,
    iconBottom: 12,
  }

  componentDidMount() {
    this.read();
    this.setIconBottomInterval();
  }

  setIconBottomInterval = () => {
    if (this.iconBottomInterval) {
      clearInterval(this.iconBottomInterval)
    }
    this.iconBottomInterval = setInterval(() => {
      this.setState({
        iconBottom: this.state.iconBottom === 20 ? 12 : 20,
      })
    }, 1500)
  }

  componentWillUnmount() {
    this.clear();
    if (this.iconBottomInterval) {
      clearInterval(this.iconBottomInterval)
    }
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

  readToTable = () => {
    const { dispatch, filePath } = this.props;
    const { currentPage, pageSize } = this.state;
    dispatch({
      type: 'hdfs/readToTable',
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
    if (value === '1') {
      this.setIconBottomInterval();
    } else {
      if (this.iconBottomInterval) {
        clearInterval(this.iconBottomInterval)
      }
    }
    if (!this.readMoreThanOnce && value === '2') {
      this.readMoreThanOnce = true;
      this.readToTable();
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
    }, this.readToTable)
  }

  getContentArr = (start, end) => {
    if (this.props.hdfs.content.length === 0) {
      return [];
    }
    if (this.contentArr === undefined) {
      this.contentArr = this.props.hdfs.content.split('\n');
      if (!this.contentArr[this.contentArr.length - 1]) {
        this.contentArr.pop();
      }
    }
    if (start !== undefined && end !== undefined) {
      if (start > end) {
        start = end;
      }
      const len = this.contentArr.length;
      if (start > len) {
        start = len - 1;
      }
      if (end > len) {
        end = len;
      }
      return this.contentArr.slice(start, end);
    }
    return this.contentArr;
  }

  changeSlider = value => {
    this.setState({
      startPoint: value[0],
      endPoint: value[1],
    })
  }

  changeJsonVisible = visible => {
    this.setState({
      jsonDialogVisible: visible,
    })
  }

  showJson = json => {
    this.setState({
      jsonLine: json,
      jsonDialogVisible: true,
    })
  }

  nextText = () => {
    this.setState({
      textLineTotal: this.state.textLineTotal + 10,
    })
  }

  render() {
    const { loading, hdfs, filePath, height, width } = this.props;
    const { content, contentColumn, contentDataSource, contentTotal } = hdfs;
    const { type, currentPage, pageSize, startPoint, endPoint, jsonLine, jsonDialogVisible, textLineTotal, iconBottom } = this.state;

    return (
      <GridContent>
        <Tabs activeKey={type} onChange={this.changeType}>
          <TabPane tab="Text" key="1">
            {type === '1' && <Skeleton loading={loading.effects['hdfs/read']} title={false} paragraph={{ rows: 4 }} active >
              {this.getContentArr(0, textLineTotal).map(line => <p key={textId++}>{line}</p>)}
              <div style={{textAlign: 'center', paddingTop: 32 - iconBottom, paddingBottom: iconBottom, transition: 'padding 1.5s'}}>
                {this.getContentArr().length > textLineTotal ? 
                  <Icon 
                    type='double-right' rotate={90} onClick={this.nextText} 
                    id={styles.bottom_icon}
                  /> :
                  <span style={{color: '#d9d9d9'}}>- End -</span>}
              </div>
            </Skeleton>}
          </TabPane>
          <TabPane tab="Table" key="2">
            {type === '2' && <Table
              columns={contentColumn}
              dataSource={contentDataSource}
              bordered
              size="middle"
              rowKey={() => tableId++}
              loading={loading.effects["hdfs/readToTable"]}
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
            />}
          </TabPane>
          <TabPane tab="Json" key="3">
            {type === '3' && <div>
              <Row>
                <Col span={1} style={{padding: '8px 0', color: '#52c41a'}}>{0}</Col>
                <Col span={22}>
                  <Slider range value={[startPoint, endPoint]} onChange={this.changeSlider} max={this.getContentArr().length} />
                </Col>
                <Col span={1} style={{textAlign: 'right', padding: '8px 0', color: '#f5222d'}}>{this.getContentArr().length}</Col>
              </Row>
              <Skeleton loading={loading.effects['hdfs/read']} title={false} paragraph={{ rows: 4 }} active >
                {this.getContentArr(startPoint, endPoint).map(line => (
                  <p key={jsonId++}>
                    <Icon type='zoom-in' style={{marginRight: 4, color: '#40a9ff'}} onClick={() => this.showJson(line)} />
                    {line}
                  </p>
                ))}
              </Skeleton>
            </div>}
          </TabPane>
        </Tabs>
        {jsonDialogVisible && <Modal
          destroyOnClose={true}
          title={`Json Format`}
          visible={jsonDialogVisible}
          onCancel={() => this.changeJsonVisible(false)}
          footer={null}
          width={600}
          style={{ top: 48 }}
          bodyStyle={{ height: height - 80, overflow: 'auto' }}
        >
          <ReactJson 
            name={null}
            src={JSON.parse(jsonLine)} 
            displayObjectSize={false} 
            displayDataTypes={false} 
            enableClipboard={false}
            collapseStringsAfterLength={50} // 以 ... 显示
            collapsed={false} // false 全展开 true 全关闭 integer 展开的深度
            indentWidth={4} // 缩进
            iconStyle='triangle' // circle square
            // theme='google'
          />
        </Modal>}
      </GridContent>
    );
  }
}

export default ContentDialog;
