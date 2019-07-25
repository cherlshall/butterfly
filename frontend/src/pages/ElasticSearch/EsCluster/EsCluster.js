import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Table, Button, Row, Col, Card, Icon, Input, Radio, Tag, Popconfirm, Progress, message } from 'antd';
import GridContent from '@/components/PageHeaderWrapper/GridContent';
import Highlighter from 'react-highlight-words';
import router from 'umi/router';
import Link from 'umi/link';
import { Pie, WaterWave, Gauge, TagCloud } from '@/components/Charts';
import ActiveChart from '@/mycomponents/ActiveChart';
import styles from './EsCluster.less';
import CreateIndexDialog from './CreateIndexDialog';

@connect(({ esCluster, loading }) => ({
  esCluster,
  loading,
}))
class EsCluster extends React.Component {

  state = {
    showClusterInfo: true,
    createIndexVisible: false,
    deleteIndexName: new Set(),
  }

  componentDidMount() {
    this.health();
    this.searchTags();
  }

  searchTags = () => {
    const { dispatch } = this.props;
    dispatch({
      type: 'esCluster/fetchTags',
    });
  }

  health = () => {
    const { dispatch } = this.props;
    dispatch({
      type: 'esCluster/health',
    })
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
      if (record.closed || record.deleted) {
        return (
          <Highlighter
            highlightStyle={{ backgroundColor: '#ffc069', padding: 0 }}
            searchWords={[this.state.searchText]}
            autoEscape
            textToHighlight={text.toString()}
            onClick={() => {message.error(record.close ? 'index is closed' : 'table is deleted')}}
            style={{cursor: "not-allowed", color: "#A9A9A9"}}
          />
        )
      } else {
        return (
          <Link to={`/elasticsearch/esIndex/${text}`}>
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

  columns = [
    {
      title: 'Index Name',
      dataIndex: 'indexName',
      key: 'indexName',
      width: 240,
      ...this.getColumnSearchProps('indexName'),
    },
    {
      title: 'Replica',
      dataIndex: 'replicaNum',
      width: 100,
    },
    {
      title: 'Status (Active shards / All shards)',
      dataIndex: 'status',
      width: 180,
      render: (text, record) => {
        const color = this.getColor(text);
        return (
          <Row gutter={12}>
            <Col span={6} style={{ textAlign: 'left', color: color, margin: '2px 0 -2px 0', fontWeight: 'bold' }}>
              {`${record.activeShardNum} / ${record.activeShardNum + record.unassignedShardNum}`}
            </Col>
            <Col span={18}>
              <Progress 
                strokeColor={color} 
                percent={record.activeShardPercent} 
                size="small" 
                status={text === 0 ? "success" : "active"} 
                style={{ width: '80%' }}
              />
            </Col>
          </Row>
        )
      }
    },
    {
      title: 'Action',
      key: 'action',
      width: 40,
      render: (text, record) => {
        const deleting = this.state.deleteIndexName.has(record.indexName);
        const popParams = {};
        if (deleting) {
          popParams.visible = false;
        }
        return (
          <Popconfirm
            title="Are you sure delete this index?"
            onConfirm={() => {this.deleteIndex(record.indexName)}}
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
          </Popconfirm>
        )
      }
    },
  ];

  getColor = status => {
    return status === 0 ? "#52c41a" : status === 1 ? "#ffc53d" : "#f5222d"
  }

  getWaterWave = (title, percent, color) => {
    return (
      <WaterWave
        height={161}
        title={title}
        percent={percent}
        color={color}
      />
    )
  }

  changeShowClusterInfo = show => {
    this.setState({
      showClusterInfo: show,
    })
  }

  changeCreateIndexVisible = visible => {
    this.setState({
      createIndexVisible: visible,
    })
  }

  deleteIndex = indexName => {
    const { dispatch } = this.props;
    this.setState({
      deleteIndexName: this.state.deleteIndexName.add(indexName),
    })
    dispatch({
      type: 'esIndex/deleteIndex',
      payload: {
        indexName,
      },
      callback: () => {
        this.state.deleteIndexName.delete(indexName);
        this.setState({
          deleteIndexName: this.state.deleteIndexName,
        })
        this.health();
      },
    });
  }

  render() {
    const { esCluster, loading } = this.props;
    const { clusterHealth, indexHealth, searchTags } = esCluster;
    const { clusterName, activeShardNum, unassignedShardNum, activeShardPercent, status, dataNodeNum, nodeNum } = clusterHealth;
    const { showClusterInfo, createIndexVisible } = this.state;

    return (
      <GridContent>
        <Row
         gutter={12}
         style={{height: showClusterInfo ? 266 : 0, marginBottom: showClusterInfo ? 12 : 0}}
         className={styles.hiddenableRow}
        >
          <Col span={6}>
            <Card
              title={`Cluster Shards`}
              bodyStyle={{ textAlign: 'center', height: 210, paddingTop: 4 }}
              bordered={false}
              loading={loading.effects['esCluster/health']}
            >
              <div 
                style={{ 
                  marginBottom: 4, 
                  color: this.getColor(status), 
                  fontWeight: 'bold', 
                  fontSize: 'large', 
                }}
              >
                {clusterName}
              </div>
              {status !== undefined &&
                this.getWaterWave(`${activeShardNum} / ${activeShardNum+unassignedShardNum}`, 
                  activeShardPercent, this.getColor(status))}
            </Card>
          </Col>
          <Col span={6}>
          <Card
            loading={loading.effects['esCluster/health']}
            bordered={false}
            title={"Nodes"}
            bodyStyle={{ textAlign: 'center', height: 210, paddingTop: 12 }}
          >
            <Pie
              hasTopLegend
              subTitle={`Total`}
              total={nodeNum}
              data={[{x: 'Data Node', y: dataNodeNum}, {x: 'Non-data Node', y: nodeNum - dataNodeNum}]}
              valueFormat={value => value}
              height={161}
              lineWidth={4}
            />
          </Card>
          </Col>
          <Col span={6}>
            <Card
              title={"Keyword Distribution"}
              bodyStyle={{ textAlign: 'center', height: 210 }}
              bordered={false}
              loading={loading.effects['esCluster/fetchTags']}
            >
              <TagCloud data={searchTags} height={161} />
            </Card>
          </Col>
          <Col span={6}>
            <Card
              title={"Traffic Monitor"}
              bodyStyle={{ height: 210, paddingTop: 4 }}
              bordered={false}
              loading={false}
            >
              <ActiveChart
                subTitle="Alarm situation"
                total="Normal traffic range"
                unit="Mbps"
              />
            </Card>
          </Col>
        </Row>
        <Row style={{marginBottom: 8}}>
          <Col span={12}>
            <Button
              type='primary'
              icon={showClusterInfo ? 'up' : 'down'}
              onClick={() => this.changeShowClusterInfo(!showClusterInfo)}
              style={{ marginRight: 8 }}
            >
              {showClusterInfo ? 'Hid Cluster Info' : 'Show Cluster Info'}
            </Button>
            <Button
              type='primary'
              onClick={() => this.changeCreateIndexVisible(true)}
            >
              Create Index
            </Button>
          </Col>
          <Col span={12}></Col>
        </Row>
        <Table
          columns={this.columns}
          dataSource={indexHealth}
          bordered
          size="middle"
          rowKey="indexName"
          loading={loading.effects["esCluster/health"]}
          pagination={{
            total: indexHealth.length,
            showSizeChanger: true,
            showQuickJumper: true,
            size: 'middle',
            showTotal: (total) => `Total ${total} items`,
            pageSizeOptions: ["10", "30", "50"],
          }}
        />
        <CreateIndexDialog
          visible={createIndexVisible} 
          closeDialog={() => this.changeCreateIndexVisible(false)}
          createOver={this.health}
        />
      </GridContent>
    )
  }
}

export default EsCluster;
