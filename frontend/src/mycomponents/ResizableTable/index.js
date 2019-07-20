import React, { PureComponent } from 'react';
import { Table } from 'antd';
import { Resizable } from 'react-resizable';
import styles from './index.less';

const ResizeableTitle = props => {
  const { onResize, width, ...restProps } = props;
  return (
    <Resizable width={width || 100} height={0} onResize={onResize}>
      <th {...restProps} />
    </Resizable>
  );
};

class TableOperation extends React.Component {

  state = {
    columns: [],
  }

  components = {
    header: {
      cell: ResizeableTitle,
    },
  };

  handleResize = index => (e, { size }) => {
    this.setState(({ columns }) => {
      const nextColumns = [...columns];
      nextColumns[index] = {
        ...nextColumns[index],
        width: size.width,
      };
      return { columns: nextColumns };
    });
  };

  handleResizeChild = (index, childIndex) => (e, { size }) => {
    this.setState(({ columns }) => {
      const nextColumns = [...columns];
      nextColumns[index].children[childIndex] = {
        ...nextColumns[index].children[childIndex],
        width: size.width,
      };
      return { columns: nextColumns };
    });
  };

  componentWillReceiveProps(nextProps) {
    if (this.props.columns !== nextProps.columns){
        if (nextProps.columns){
          this.setState({columns: nextProps.columns});
        }
    }
  }

  shouldComponentUpdate(newProps, newState) {
    return true;
  }

  render() {

    const { columns, ...rest } = this.props;
    
    const columnsResize = this.state.columns.map((col, index) => {
      if (col.children) {
        for(let i = 0; i < col.children.length; i++) {
          col.children[i] = {
            ...col.children[i],
            onHeaderCell: column => {
              return ({
                width: column.width,
                onResize: this.handleResizeChild(index, i),
              })
            }
          }
        }
        return col;
      } else {
        return ({
          ...col,
          onHeaderCell: column => {
            return ({
              width: column.width,
              onResize: this.handleResize(index),
            })
          }
        })
      }
    })

    return (
      <Table
        columns={columnsResize}
        components={this.components}
        {...rest}
      />
    )
  }
}

export default TableOperation;
