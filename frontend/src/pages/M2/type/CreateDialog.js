import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Form, Input, Icon, Button, InputNumber } from 'antd';

@connect(({ m2Type, loading }) => ({
  m2Type,
  loading,
}))
class CreateDialog extends PureComponent {
  submit = values => {
    const { dispatch, createOver, editMode, record } = this.props;
    dispatch({
      type: editMode ? 'm2Type/update' : 'm2Type/create',
      payload: {
        ...record,
        ...values,
      },
      callback: createOver,
    });
  };

  handleSubmit = e => {
    e.preventDefault();
    const { form } = this.props;
    form.validateFields((err, values) => {
      if (!err) {
        this.submit(values);
      }
    });
  };

  getSubmitText = () => {
    const { editMode } = this.props;
    return editMode ? '更新' : '创建';
  };

  isTlvOrStruct = record => {
    return record.cnName === 'TLV' || record.cnName === '结构体';
  };

  render() {
    const { loading, form, record, editMode } = this.props;
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
        <Form.Item {...formItemLayout} label="中文名" required key="cnName">
          {getFieldDecorator('cnName', {
            validateTrigger: ['onChange', 'onBlur'],
            initialValue: record.cnName,
            rules: [
              {
                required: true,
                whitespace: true,
                message: '请输入中文名',
              },
            ],
          })(
            <Input
              disabled={this.isTlvOrStruct(record)}
              placeholder="中文名"
              style={{ width: '80%', marginRight: 8 }}
            />
          )}
        </Form.Item>
        <Form.Item {...formItemLayout} label="英文名" required key="enName">
          {getFieldDecorator('enName', {
            validateTrigger: ['onChange', 'onBlur'],
            initialValue: record.enName,
            rules: [
              {
                required: true,
                whitespace: true,
                message: '请输入英文名',
              },
            ],
          })(
            <Input
              disabled={this.isTlvOrStruct(record)}
              placeholder="英文名"
              style={{ width: '80%', marginRight: 8 }}
            />
          )}
        </Form.Item>
        {editMode && (
          <Form.Item {...formItemLayout} label="显示顺序" required key="displayOrder">
            {getFieldDecorator('displayOrder', {
              validateTrigger: ['onChange', 'onBlur'],
              initialValue: record.displayOrder === undefined ? '' : `${record.displayOrder}`,
              rules: [
                {
                  required: true,
                  whitespace: true,
                  message: '请输入显示顺序',
                },
              ],
            })(<InputNumber placeholder="显示顺序" style={{ width: '80%', marginRight: 8 }} />)}
          </Form.Item>
        )}
        <Form.Item {...formItemLayoutWithOutLabel}>
          <Button
            type="primary"
            disabled={loading.effects[editMode ? 'm2Protocol/update' : 'm2Protocol/create']}
            htmlType="submit"
            style={{ width: '80%' }}
          >
            {loading.effects[editMode ? 'm2Protocol/update' : 'm2Protocol/create'] ? (
              <Icon type="loading" />
            ) : (
              this.getSubmitText()
            )}
          </Button>
        </Form.Item>
      </Form>
    );
  }
}

export default Form.create({ name: 'dynamic_form_item' })(CreateDialog);
