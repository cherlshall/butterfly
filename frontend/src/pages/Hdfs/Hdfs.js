import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Table, Button, Row, Col, Icon, Input, Modal, Popconfirm, message } from 'antd';
import GridContent from '@/components/PageHeaderWrapper/GridContent';
import styles from './Hdfs.less';
import moment from 'moment';
import CreateDialog from './CreateDialog';
import MkdirDialog from './MkdirDialog';
import ContentDialog from './ContentDialog';
import { formatTrafic } from '@/utils/utils';

const { Search } = Input;

@connect(({ hdfs, loading }) => ({
  hdfs,
  loading,
}))
class Hdfs extends React.Component {

  state = {
    currentPath: '/',
    truePath: '/',
    currentPage: 1,
    pageSize: 10,
    deletePath: new Set(),
    createDialogVisible: false,
    mkdirDialogVisible: false,
    contentDialogVisible: false,
    filePath: '',
    height: document.body.clientHeight,
    width: document.body.clientWidth,
  }

  componentDidMount() {
    this.listDetail();
    window.onresize = () => {
      this.setState({
        height: document.body.clientHeight,
        width: document.body.clientWidth,
      })
    }
  }

  componentWillUnmount() {
    window.onresize = "";
  }

  listDetail = (path) => {
    const { dispatch } = this.props;
    const { currentPath } = this.state;
    dispatch({
      type: 'hdfs/detail',
      payload: {
        parent: path || currentPath,
      },
      callback: () => {
        this.setState({
          truePath: this.getTruePath(path || currentPath),
        })
      }
    })
  }

  getTruePath = path => {
    if (path === '/' || path.indexOf('/', path.length - 1) === -1) {
      return path;
    }
    return path.substring(0, path.length - 1);
  }

  getLastPath = path => {
    if (path === '/') {
      return '/';
    }
    let index = 1;
    for (let i = path.length - 1; i > 0; i--) {
      if (path[i] === '/') {
        index = i;
        break;
      }
    }
    return path.substring(0, index);
  }

  backLastPath = path => {
    const lastPath = this.getLastPath(path || this.state.truePath);
    this.setState({
      currentPath: lastPath,
    })
    this.listDetail(lastPath);
  }

  reloadPath = path => {
    this.setState({
      currentPath: path || this.state.truePath,
    })
    this.listDetail(path || this.state.truePath);
  }

  onClickDirName = path => {
    this.setState({
      currentPath: path,
    })
    this.listDetail(path);
  }

  delFile = path => {
    this.setState({
      deletePath: this.state.deletePath.add(path),
    })
    const { dispatch } = this.props;
    dispatch({
      type: 'hdfs/del',
      payload: {
        path,
      },
      callback: () => {
        const deletePath = this.state.deletePath;
        deletePath.delete(path);
        this.setState({
          deletePath,
        })
      },
    })
  }

  changeCreateVisible = visible => {
    this.setState({
      createDialogVisible: visible,
    })
  }

  changeMkdirVisible = visible => {
    this.setState({
      mkdirDialogVisible: visible,
    })
  }

  changeContentVisible = visible => {
    this.setState({
      contentDialogVisible: visible,
    })
  }

  createOver = () => {
    this.changeCreateVisible(false);
    this.reloadPath();
  }

  mkdirOver = () => {
    this.changeMkdirVisible(false);
    this.reloadPath();
  }

  onClickFileName = (filePath) => {
    this.setState({
      filePath,
      contentDialogVisible: true,
    })
  }

  pathColumns = [
    {
      title: 'Name',
      dataIndex: 'name',
      width: 120,
      render: (text, record) => {
        return (
          <div>
            <Icon type={record.file ? 'file' : 'folder'} style={{marginRight: 2, color: '#acacac'}} />
            <span 
              style={{cursor: 'pointer', color: '#40a9ff'}}
              onClick={record.file ? () => this.onClickFileName(record.path) : () => this.onClickDirName(record.path)}
            >
              {text}
            </span>
          </div>
        )
      }
    },
    {
      title: 'Size',
      dataIndex: 'len',
      width: 120,
      render: text => formatTrafic(text)
    },
    {
      title: 'Modification Time',
      dataIndex: 'modificationTime',
      width: 120,
      render: text => moment(text).format("YYYY-MM-DD HH:mm:ss")
    },
    {
      title: 'Permission',
      dataIndex: 'permission',
      width: 120,
    },
    {
      title: 'Group',
      dataIndex: 'group',
      width: 120,
    },
    {
      title: 'Owner',
      dataIndex: 'owner',
      width: 120,
    },
    {
      title: 'Block Size',
      dataIndex: 'blockSize',
      width: 120,
      render: text => formatTrafic(text)
    },
    {
      title: 'Action',
      key: 'action',
      width: 80,
      render: (text, record) => {
        const deleting = this.state.deletePath.has(record.path);
        const disabled = record.deleted || deleting;
        const popParams = {};
        if (disabled) {
          popParams.visible = false;
        }
        return (
          <Popconfirm
              title={`Are you sure delete this ${record.file ? 'file' : 'dir'}?`}
              onConfirm={() => this.delFile(record.path)}
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
                icon={record.deleted ? 'close-circle' : 'delete'}
                loading={deleting}
              >
                {record.deleted ? "Deleted" : "Delete"}
              </Button>
            </Popconfirm>
        )
      }
    }
  ]

  changePath = e => {
    this.setState({
      currentPath: e.target.value,
    })
  }

  goPath = value => {
    this.listDetail(value);
  }

  render() {
    const { hdfs, loading } = this.props;
    const { detailList } = hdfs;
    const { currentPath, truePath, createDialogVisible, mkdirDialogVisible, contentDialogVisible, filePath } = this.state;

    return (
      <GridContent>
        <Row style={{marginBottom: 12}}>
          <Col span={18}>
            <Search
              value={currentPath}
              onChange={this.changePath}
              placeholder="input path"
              enterButton="Go!"
              onSearch={this.goPath}
            />
          </Col>
          <Col span={6} style={{textAlign: 'right'}}>
            {truePath !== '/' && 
            <Button type='primary' icon='rollback' onClick={() => this.backLastPath(truePath)} style={{marginRight: 4}} />}
            <Button type='primary' icon='reload' onClick={() => this.reloadPath(truePath)} style={{marginRight: 4}} />
            <Button type='primary' icon='folder-add' onClick={() => this.changeMkdirVisible(true)} style={{marginRight: 4}} />
            <Button type='primary' icon='file-add' onClick={() => this.changeCreateVisible(true)} style={{marginRight: 4}} />
            <Button type='primary' icon='upload' />
          </Col>
        </Row>
        <Table
          columns={this.pathColumns}
          dataSource={detailList}
          bordered
          size="middle"
          rowKey="name"
          loading={loading.effects["hdfs/detail"]}
          pagination={{
            total: detailList.length,
            showSizeChanger: true,
            showQuickJumper: true,
            size: 'middle',
            showTotal: (total) => `Total ${total} items`,
            pageSizeOptions: ["10", "30", "50"],
          }}
        />
        <Modal
          // destroyOnClose={true}
          title={`Create file`}
          visible={createDialogVisible}
          onCancel={() => this.changeCreateVisible(false)}
          footer={null}
        >
          <CreateDialog onOver={this.createOver} path={truePath} />
        </Modal>
        <Modal
          // destroyOnClose={true}
          title={`Mkdir`}
          visible={mkdirDialogVisible}
          onCancel={() => this.changeMkdirVisible(false)}
          footer={null}
        >
          <MkdirDialog onOver={this.mkdirOver} path={truePath} />
        </Modal>
        <Modal
          destroyOnClose={true}
          title={filePath}
          visible={contentDialogVisible}
          onCancel={() => this.changeContentVisible(false)}
          footer={null}
          width={this.state.width - 48}
          style={{ top: 24 }}
          bodyStyle={{ height: this.state.height - 48 - 55, overflow: 'auto' }}
        >
          <ContentDialog filePath={filePath} />
        </Modal>
      </GridContent>
    )
  }
}

export default Hdfs;
