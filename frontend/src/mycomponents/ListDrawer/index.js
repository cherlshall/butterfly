import React, { PureComponent } from 'react';
import { Drawer, Input, Icon, Button, Spin } from 'antd';
import GridContent from '@/components/PageHeaderWrapper/GridContent';
import styles from './index.less';

class ListDrawer extends PureComponent {

  state = {
    filterText: '',
  }

  componentDidMount() {
  }

  render() {

    const { title, loading, onReload, visible, onClose, dataSource, selected, onSelect } = this.props;
    const { filterText } = this.state;

    return (
      <Drawer
        title={
          <div>
            <span style={{display: "inline-block", width: "70%"}}>{title}</span>
            <span style={{display: "inline-block", width: "30%", textAlign: "right"}}>
              <Icon type="reload" style={{cursor: "pointer", color: "#096dd9"}} onClick={onReload} />
            </span>
            <Input 
              style={{marginTop: 12}} 
              allowClear 
              onChange={(e) => {this.setState({filterText: e.target.value})}}
              placeholder="For Search"
              prefix={<Icon type="search" style={{ color: 'rgba(0,0,0,.25)'}} />}
            />
          </div>
        }
        placement="right"
        closable={false}
        onClose={onClose}
        visible={visible}
      >
        <Spin spinning={loading}>
          {dataSource.filter(item => {
            if (item.indexOf(filterText) !== -1) {
              return true;
            } else {
              return false;
            }
          }).map(item => (
            <p 
              key={item}
              className={selected === item ? styles.listDrawer_list_item_select : styles.listDrawer_list_item}
              onClick={() => onSelect(item)}
            >
              {item}
            </p>
          ))}
        </Spin>
      </Drawer>
    );
  }
}

export default ListDrawer;
