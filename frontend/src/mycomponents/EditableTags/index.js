import React, { Component } from 'react';
import { Tag, Icon, Button, Tooltip, Input } from 'antd';
import styles from './index.less';

export default class EditableTags extends Component {

  state = {
    initTags: [],
    additionTagsValue: [],
    inputVisible: false,
    inputValue: '',
  }

  componentDidMount() {
    this.initTagsFromProps();
  }
  
  componentWillReceiveProps(newProps) {
    if (newProps.initValues !== this.props.initValues) {
      this.initTagsFromProps();
    }
  }

  initTagsFromProps = () => {
    const initTags = [];
    this.props.initValues.forEach(value => {
      initTags.push({
        value,
        closed: false,
      })
    })
    this.setState({
      initTags,
    })
  }

  indexOfTags = (arr, value) => {
    for (let i = 0; i < arr.length; i++) {
      if (arr[i].value === value) {
        return i;
      }
    }
    return -1;
  }

  handleClose = (tagValue, fromInit) => {
    const { initTags, additionTagsValue } = this.state;
    if (fromInit) {
      initTags[this.indexOfTags(initTags, tagValue)].closed = true;
      this.setState({
        initTags,
      })
    } else {
      additionTagsValue.splice(additionTagsValue.indexOf(tagValue), 1);
      this.setState({
        additionTagsValue,
      })
    }
  };

  showInput = () => {
    this.setState({ inputVisible: true }, () => this.input.focus());
  };

  handleInputChange = e => {
    this.setState({ inputValue: e.target.value });
  };

  handleInputConfirm = () => {
    const { inputValue } = this.state;
    const { initTags, additionTagsValue } = this.state;
    if (inputValue) {
      const indexOfInit = this.indexOfTags(initTags, inputValue);
      const indexOfAddition = additionTagsValue.indexOf(inputValue);
      if (indexOfInit !== -1) {
        initTags[indexOfInit].closed = false;
      } else if(indexOfAddition === -1) {
        additionTagsValue.push(inputValue)
      }
    }
    this.setState({
      initTags, 
      additionTagsValue,
      inputVisible: false,
      inputValue: '',
    });
  };

  save = () => {
    const { save } = this.props;
    if (save) {
      const { initTags, additionTagsValue } = this.state;
      const deleteValue = [];
      initTags.forEach(tag => {
        if (tag.closed) {
          deleteValue.push(tag.value);
        }
      })
      save(deleteValue, additionTagsValue);
    }
  }

  saveInputRef = input => (this.input = input);

  render() {
    const { saving } = this.props;
    const { initTags, additionTagsValue, inputVisible, inputValue } = this.state;

    return (
      <div>
        {initTags.map(tag => {
          const { value, closed } = tag;
          const isLongTag = value.length > 10;
          const tagElem = closed ? (
            <Tag 
              key={`init_${value}`} 
              color='red' 
              visible={true}
            >
              {isLongTag ? `${value.slice(0, 10)}...` : value}
            </Tag>
          ) : (
            <Tag 
              key={`init_${value}`} 
              closable
              color='blue' 
              visible={true}
              onClose={() => this.handleClose(value, true)}
            >
              {isLongTag ? `${value.slice(0, 10)}...` : value}
            </Tag>
          );
          return isLongTag ? (
            <Tooltip title={value} key={`init_${value}`}>
              {tagElem}
            </Tooltip>
          ) : (
            tagElem
          );
        })}
        {additionTagsValue.map(value => {
          const isLongTag = value.length > 20;
          const tagElem = (
            <Tag 
              key={`add_${value}`} 
              closable
              color='green'
              onClose={() => this.handleClose(value, false)}
            >
              {isLongTag ? `${value.slice(0, 20)}...` : value}
            </Tag>
          );
          return isLongTag ? (
            <Tooltip title={value} key={`add_${value}`}>
              {tagElem}
            </Tooltip>
          ) : (
            tagElem
          );
        })}
        {inputVisible && (
          <Input
            ref={this.saveInputRef}
            type="text"
            size="small"
            style={{ width: 78, marginRight: 8 }}
            value={inputValue}
            onChange={this.handleInputChange}
            onBlur={this.handleInputConfirm}
            onPressEnter={this.handleInputConfirm}
          />
        )}
        {!inputVisible && (
          <Tag onClick={this.showInput} style={{ background: '#fff', borderStyle: 'dashed' }}>
            <Icon type="plus" /> New Tag
          </Tag>
        )}
        <Button type="primary" size='small' icon='save' loading={!!saving} onClick={this.save}>
          Save
        </Button>
      </div>
    );
  }
}
