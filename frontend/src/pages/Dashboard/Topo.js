import React, { Component, Suspense } from 'react';
import { connect } from 'dva';
import { Card, Upload, Button, message } from 'antd';
import G6 from '@antv/g6';
import { upload } from '@/services/account';
import reqwest from 'reqwest';

// const data = {
//   // 点集
//   nodes: [
//     {
//       id: 'node1', // String，该节点存在则必须，节点的唯一标识
//       x: 0, // Number，可选，节点位置的 x 值
//       y: 0, // Number，可选，节点位置的 y 值
//     },
//     {
//       id: 'node2', // String，该节点存在则必须，节点的唯一标识
//       x: 300, // Number，可选，节点位置的 x 值
//       y: 200, // Number，可选，节点位置的 y 值
//     },
//   ],
//   // 边集
//   edges: [
//     {
//       source: 'node1', // String，必须，起始点 id
//       target: 'node2', // String，必须，目标点 id
//     },
//   ],
// };
const data = {};

@connect(({ m2Field, loading }) => ({
  m2Field,
  loading,
}))
class Topo extends Component {
  componentDidMount() {
    // const graph = new G6.Graph({
    //   container: 'mountNode', // String | HTMLElement，必须，在 Step 1 中创建的容器 id 或容器本身
    //   width: 1405, // Number，必须，图的宽度
    //   height: 1405, // Number，必须，图的高度
    //   // layout: {type: 'force'}
    // });
    // graph.data(data); // 读取 Step 2 中的数据源到图上
    // graph.render(); // 渲染图
  }

  render() {
    const props = {
      name: 'file',
      action: 'http://localhost:9000/server/account/upload',
      headers: {
        authorization: 'authorization-text',
      },
      // beforeUpload: file => {
      //   const data = new FormData();
      //   data.append('file', file);//名字和后端接口名字对应
      //   reqwest({
      //     url: 'server/account/upload',
      //     method: 'post',
      //     processData: false,
      //     data: data,
      //     success: () => {},
      //     error: () => {},
      //   });
      //   return false;
      // },
      onChange(info) {
        if (info.file.status !== 'uploading') {
          console.log(info.file, info.fileList);
        }
        if (info.file.status === 'done') {
          message.success(`${info.file.name} file uploaded successfully`);
        } else if (info.file.status === 'error') {
          message.error(`${info.file.name} file upload failed.`);
        }
      },
    };

    return (
      <Card>
        <div id="mountNode" />
        <Upload {...props}>
          <Button>Click to Upload</Button>
        </Upload>
      </Card>
    );
  }
}

export default Topo;
