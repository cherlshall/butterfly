import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Drawer, Button, Row, Col, Spin, Icon, Input, Radio } from 'antd';
import GridContent from '@/components/PageHeaderWrapper/GridContent';
import ResizableTable from "@/mycomponents/ResizableTable";

@connect(({ adminOperation, loading }) => ({
  adminOperation,
  loading,
}))
class AdminOperation extends PureComponent {

  state = {
    
  }

  componentDidMount() {
  }


  render() {

    const { adminOperation, loading } = this.props;
    const {  } = adminOperation;

    return (
      <GridContent>
        admin operation
      </GridContent>
    )
  }
}

export default AdminOperation;
