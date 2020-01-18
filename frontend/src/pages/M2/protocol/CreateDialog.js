import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Form, Input, Icon, Button, Select } from 'antd';

const { Option } = Select;

@connect(({ m2Protocol, loading }) => ({
  m2Protocol,
  loading,
}))
class CreateDialog extends PureComponent {
  componentDidMount() {
    this.loadProtocolNames();
  }

  loadProtocolNames = () => {
    const { dispatch } = this.props;
    dispatch({
      type: 'm2Protocol/listProtocolName',
      payload: {
        category: 1,
      },
    });
  };

  submit = values => {
    const { dispatch, createOver, category, editMode, record } = this.props;
    dispatch({
      type: editMode ? 'm2Protocol/update' : 'm2Protocol/create',
      payload: {
        ...record,
        ...values,
        category,
      },
      callback: createOver,
    });
  };

  handleSubmit = e => {
    e.preventDefault();
    const { form } = this.props;
    form.validateFields((err, values) => {
      const valuesSubmit = { ...values };
      if (values.type.indexOf('0x') === 0 || values.type.indexOf('0X') === 0) {
        valuesSubmit.type = parseInt(values.type, 16);
      }
      if (!err) {
        this.submit(valuesSubmit);
      }
    });
  };

  render() {
    const { loading, form, record, editMode, category, m2Protocol } = this.props;
    const { names } = m2Protocol;
    const { getFieldDecorator } = form;
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
    return (
      <Form onSubmit={this.handleSubmit}>
        <Form.Item {...formItemLayout} label="Type" required key="type">
          {getFieldDecorator('type', {
            validateTrigger: ['onChange', 'onBlur'],
            initialValue: record.type,
            rules: [
              {
                required: true,
                whitespace: true,
                message: 'Please input protocol type.',
              },
            ],
          })(<Input placeholder="protocol type" style={{ width: '80%', marginRight: 8 }} />)}
        </Form.Item>
        {category !== 1 && (
          <Form.Item {...formItemLayout} label="Protocol" required key="protocolId">
            {getFieldDecorator('protocolId', {
              validateTrigger: ['onChange', 'onBlur'],
              initialValue: record.protocolId === undefined ? '' : record.protocolId.toString(),
              rules: [
                {
                  required: true,
                  whitespace: true,
                  message: 'Please input name of protocol.',
                },
              ],
            })(
              <Select
                style={{ width: '80%', marginRight: 8 }}
                showSearch
                placeholder="Select name of protocol"
              >
                {names.map(name => (
                  <Option value={name.id.toString()} key={name.id}>
                    {name.enName}
                  </Option>
                ))}
              </Select>
            )}
          </Form.Item>
        )}
        <Form.Item {...formItemLayout} label="Name(CN)" required key="cnName">
          {getFieldDecorator('cnName', {
            validateTrigger: ['onChange', 'onBlur'],
            initialValue: record.cnName,
            rules: [
              {
                required: true,
                whitespace: true,
                message: 'Please input protocol Name(CN).',
              },
            ],
          })(<Input placeholder="protocol name(CN)" style={{ width: '80%', marginRight: 8 }} />)}
        </Form.Item>
        <Form.Item {...formItemLayout} label="Name(EN)" required key="enName">
          {getFieldDecorator('enName', {
            validateTrigger: ['onChange', 'onBlur'],
            initialValue: record.enName,
            rules: [
              {
                required: true,
                whitespace: true,
                message: 'Please input protocol Name(EN).',
              },
            ],
          })(<Input placeholder="protocol name(EN)" style={{ width: '80%', marginRight: 8 }} />)}
        </Form.Item>
        <Form.Item {...formItemLayout} label="Description" key="description">
          {getFieldDecorator('description', {
            validateTrigger: ['onChange', 'onBlur'],
            initialValue: record.description,
          })(<Input placeholder="protocol description" style={{ width: '80%', marginRight: 8 }} />)}
        </Form.Item>
        <Form.Item {...formItemLayoutWithOutLabel}>
          <Button
            type="primary"
            disabled={loading.effects[editMode ? 'm2Protocol/update' : 'm2Protocol/create']}
            htmlType="submit"
            style={{ width: '80%' }}
          >
            {loading.effects[editMode ? 'm2Protocol/update' : 'm2Protocol/create'] ? (
              <Icon type="loading" />
            ) : editMode ? (
              'Update'
            ) : (
              'Create'
            )}
          </Button>
        </Form.Item>
      </Form>
    );
  }
}

export default Form.create({ name: 'dynamic_form_item' })(CreateDialog);
