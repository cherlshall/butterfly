import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Table, Button, Row, Col, Card, Icon, Input, Radio, Tag, Popconfirm, Progress, message } from 'antd';
import GridContent from '@/components/PageHeaderWrapper/GridContent';
import Highlighter from 'react-highlight-words';
import router from 'umi/router';
import Link from 'umi/link';
import { Pie, WaterWave, Gauge, TagCloud } from '@/components/Charts';
import ActiveChart from '@/mycomponents/ActiveChart';
import styles from './EsIndex.less';

@connect(({ esIndex, loading }) => ({
  esIndex,
  loading,
}))
class EsIndex extends React.Component {

  state = {
    currentIndexName: '',
  }

  componentDidMount() {
    const { indexName } = this.props.match.params;
    this.setState({
      currentIndexName: indexName,
    })
  }

  render() {
    const { esIndex, loading } = this.props;
    const {  } = esIndex;
    const { currentIndexName } = this.state;

    return (
      <GridContent>
        {currentIndexName || 'ExIndex'}
      </GridContent>
    )
  }
}

export default EsIndex;
