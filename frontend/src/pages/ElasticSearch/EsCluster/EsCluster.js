import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Table, Button, Row, Col, Switch, Icon, Input, Radio, Tag, Popconfirm, Progress, message } from 'antd';
import GridContent from '@/components/PageHeaderWrapper/GridContent';
import Highlighter from 'react-highlight-words';
import router from 'umi/router';
import Link from 'umi/link';

@connect(({ esCluster, loading }) => ({
  esCluster,
  loading,
}))
class EsCluster extends PureComponent {

  state = {
  }

  componentDidMount() {
    this.health();
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
      width: 180,
      ...this.getColumnSearchProps('indexName'),
    },
    {
      title: 'Replica',
      dataIndex: 'replicaNum',
      width: 80,
    },
    {
      title: 'Status(Active shards / All shards)',
      dataIndex: 'status',
      width: 160,
      render: (text, record) => {
        const color = text === 0 ? "#52c41a" : text === 1 ? "#ffc53d" : "#f5222d";
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
      width: 120,
      render: (text, record) => (
        <Popconfirm
          title="Are you sure delete this index?"
          onConfirm={() => {}}
          okText="Yes"
          cancelText="No"
        >
          <Button 
            style={{cursor: "pointer"}} 
            type="danger" 
            size="small"
            icon={'delete'}
          >
            {"Delete"}
          </Button>
        </Popconfirm>
      )
    },
  ];

  render() {
    const { esCluster, loading } = this.props;
    const { clusterHealth, indexHealth } = esCluster;

    return (
      <GridContent>
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
      </GridContent>
    )
  }
}

export default EsCluster;
