import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Form, Input, Icon, Button } from 'antd';
import GridContent from '@/components/PageHeaderWrapper/GridContent';
import styles from './AdminOperation.less';

let id = 0;

@connect(({ adminOperation, loading }) => ({
  adminOperation,
  loading,
}))
class CreateTableDialog extends PureComponent {

  componentDidMount() {
    this.add();
  }

  create = (tableName, families) => {
    const { dispatch } = this.props;
    const fArr = [];
    new Set(families || []).forEach((value) => {
      fArr.push(value);
    });
    dispatch({
      type: 'adminOperation/create',
      payload: {
        tableName,
        families: fArr,
      },
      callback: this.props.createOver,
    });
  }

  remove = k => {
    const { form } = this.props;
    // can use data-binding to get
    const keys = form.getFieldValue('keys');
    // We need at least one passenger
    if (keys.length === 1) {
      return;
    }

    // can use data-binding to set
    form.setFieldsValue({
      keys: keys.filter(key => key !== k),
    });
  };

  add = () => {
    const { form } = this.props;
    // can use data-binding to get
    const keys = form.getFieldValue('keys');
    const nextKeys = keys.concat(id++);
    // can use data-binding to set
    // important! notify form to detect changes
    form.setFieldsValue({
      keys: nextKeys,
    });
  };

  handleSubmit = e => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
        const { tableName, keys, families } = values;
        console.log('Received values of form: ', values);
        console.log('Merged values:', keys.map(key => families[key]));
        this.create(tableName, families);
      }
    });
  };

  render() {
    const { adminOperation, loading } = this.props;
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
        label={index === 0 ? 'Family' : ''}
        required={true}
        key={k}
      >
        {getFieldDecorator(`families[${k}]`, {
          validateTrigger: ['onChange', 'onBlur'],
          rules: [
            {
              required: true,
              whitespace: true,
              message: keys.length === 1 ? "Please input family's name." : "Please input family's name or delete this field.",
            },
          ],
        })(<Input placeholder="family name" style={{ width: '80%', marginRight: 8 }} />)}
        {keys.length > 1 ? (
          <Icon
            className="dynamic-delete-button"
            type="minus-circle-o"
            onClick={() => this.remove(k)}
          />
        ) : null}
      </Form.Item>
    ));
    return (
      <Form onSubmit={this.handleSubmit}>
        <Form.Item
          {...formItemLayout}
          label={'Table Name'}
          required={true}
          key={'tableName'}
        >
          {getFieldDecorator('tableName', {
            validateTrigger: ['onChange', 'onBlur'],
            rules: [
              {
                required: true,
                whitespace: true,
                message: "Please input table's name.",
              },
            ],
          })(<Input placeholder="table name" style={{ width: '80%', marginRight: 8 }} />)}
        </Form.Item>
        {formItems}
        <Form.Item {...formItemLayoutWithOutLabel}>
          <Button type="dashed" onClick={this.add} style={{ width: '80%' }}>
            <Icon type="plus" /> Add family
          </Button>
        </Form.Item>
        <Form.Item {...formItemLayoutWithOutLabel}>
          <Button type="primary" disabled={loading.effects["adminOperation/create"]} htmlType="submit" style={{ width: '80%' }}>
            {loading.effects["adminOperation/create"] ? <Icon type="loading" /> : "Create"}
          </Button>
        </Form.Item>
      </Form>
    );
  }
}

export default Form.create({ name: 'dynamic_form_item' })(CreateTableDialog);
