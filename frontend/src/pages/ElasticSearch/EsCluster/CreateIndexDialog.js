import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Form, Input, Icon, Button, Modal, Select } from 'antd';
import GridContent from '@/components/PageHeaderWrapper/GridContent';

let id = 0;

const { Option, OptGroup } = Select;

@connect(({ esIndex, loading }) => ({
  esIndex,
  loading,
}))
class CreateIndexDialog extends PureComponent {

  componentDidMount() {
    
  }

  create = (indexName, properties, types) => {
    const { dispatch } = this.props;
    const params = {};
    if (properties && properties.length > 0) {
      const propsWithType = {};
      for (let i = 0; i < properties.length; i++) {
        propsWithType[properties[i]] = types[i];
      }
      params.properties = propsWithType;
    }
    dispatch({
      type: 'esIndex/createIndex',
      payload: {
        indexName,
        ...params,
      },
      callback: () => {
        this.props.closeDialog();
        if (this.props.createOver) {
          this.props.createOver();
        }
      }
    });
    
  }

  remove = k => {
    const { form } = this.props;
    const keys = form.getFieldValue('keys');
    form.setFieldsValue({
      keys: keys.filter(key => key !== k),
    });
  };

  add = () => {
    const { form } = this.props;
    const keys = form.getFieldValue('keys');
    const nextKeys = keys.concat(id++);
    form.setFieldsValue({
      keys: nextKeys,
    });
  };

  handleSubmit = e => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
        const { indexName, keys, properties, types } = values;
        this.create(indexName, properties, types);
      }
    });
  };

  types = {string: ["keyword", "text"], number: ["integer", "long", "double", "float"], boolean: ["boolean"]};

  typeSelect = (key) => {
    const { getFieldDecorator } = this.props.form;
    return (
      getFieldDecorator(`types[${key}]`, {
        initialValue: 'keyword',
      })(
        <Select style={{ width: 100 }} dropdownMatchSelectWidth={false}>
          {Object.keys(this.types).map(typeKey => (
            <OptGroup key={typeKey} label={typeKey}>
              {this.types[typeKey].map(typeItem => {
                return (
                  <Option key={typeItem} value={typeItem}>{typeItem}</Option>
                )})}
            </OptGroup>
          ))}
        </Select>
      )
    )
  }

  render() {
    const { esIndex, loading, visible, closeDialog } = this.props;
    const { getFieldDecorator, getFieldValue } = this.props.form;
    const formItemLayout = {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 18 },
      },
    };
    const formItemLayoutWithOutLabel = {
      wrapperCol: {
        xs: { span: 24, offset: 0 },
        sm: { span: 18, offset: 6 },
      },
    };
    getFieldDecorator('keys', { initialValue: [] });
    const keys = getFieldValue('keys');
    const formItems = keys.map((k, index) => (
      <Form.Item
        {...(index === 0 ? formItemLayout : formItemLayoutWithOutLabel)}
        label={index === 0 ? 'Property' : ''}
        required={true}
        key={k}
      >
        {getFieldDecorator(`properties[${k}]`, {
          validateTrigger: ['onChange', 'onBlur'],
          rules: [
            {
              required: true,
              whitespace: true,
              message: "Please input property or delete this field.",
            },
          ],
        })(<Input placeholder="property name" addonAfter={this.typeSelect(k)} style={{ width: '80%', marginRight: 8 }} />)}
        {(
          <Icon
            className="dynamic-delete-button"
            type="minus-circle-o"
            onClick={() => this.remove(k)}
          />
        )}
      </Form.Item>
    ));
    return (
      <Modal
        title="Create Index"
        visible={visible}
        onCancel={closeDialog}
        footer={null}
      >
        <Form onSubmit={this.handleSubmit}>
          <Form.Item
            {...formItemLayout}
            label={'Index Name'}
            required={true}
            key={'indexName'}
          >
            {getFieldDecorator('indexName', {
              validateTrigger: ['onChange', 'onBlur'],
              rules: [
                {
                  required: true,
                  whitespace: true,
                  message: "Please input index name.",
                },
              ],
            })(<Input placeholder="index name" style={{ width: '80%', marginRight: 8 }} />)}
          </Form.Item>
          {formItems}
          <Form.Item {...formItemLayoutWithOutLabel}>
            <Button type="dashed" onClick={this.add} style={{ width: '80%' }}>
              <Icon type="plus" /> Add property
            </Button>
          </Form.Item>
          <Form.Item {...formItemLayoutWithOutLabel}>
            <Button type="primary" disabled={loading.effects["esIndex/createIndex"]} htmlType="submit" style={{ width: '80%' }}>
              {loading.effects["esIndex/createIndex"] ? <Icon type="loading" /> : "Create"}
            </Button>
          </Form.Item>
        </Form>
      </Modal>
    );
  }
}

export default Form.create({ name: 'es_create_index_form' })(CreateIndexDialog);
