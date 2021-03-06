import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Form, Input, Icon, Button } from 'antd';
// import styles from './HBaseAdmin.less';

let id = 0;

@connect(({ hbaseAdmin, loading }) => ({
  hbaseAdmin,
  loading,
}))
class CreateTableDialog extends PureComponent {
  componentDidMount() {
    this.add();
  }

  create = (tableName, families) => {
    const { dispatch, createOver } = this.props;
    const fArr = [];
    new Set(families || []).forEach(value => {
      fArr.push(value);
    });
    dispatch({
      type: 'hbaseAdmin/create',
      payload: {
        tableName,
        families: fArr,
      },
      callback: createOver,
    });
  };

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
    id += 1;
    const nextKeys = keys.concat(id);
    // can use data-binding to set
    // important! notify form to detect changes
    form.setFieldsValue({
      keys: nextKeys,
    });
  };

  handleSubmit = e => {
    e.preventDefault();
    const { form } = this.props;
    form.validateFields((err, values) => {
      if (!err) {
        const { tableName, keys, families } = values;
        const familiesByKey = [];
        keys.forEach(k => {
          familiesByKey.push(families[k]);
        });
        this.create(tableName, familiesByKey);
      }
    });
  };

  render() {
    const { loading, form } = this.props;
    const { getFieldDecorator, getFieldValue } = form;
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
        required
        key={k}
      >
        {getFieldDecorator(`families[${k}]`, {
          validateTrigger: ['onChange', 'onBlur'],
          rules: [
            {
              required: true,
              whitespace: true,
              message:
                keys.length === 1
                  ? "Please input family's name."
                  : "Please input family's name or delete this field.",
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
        <Form.Item {...formItemLayout} label="Table Name" required key="tableName">
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
          <Button
            type="primary"
            disabled={loading.effects['hbaseAdmin/create']}
            htmlType="submit"
            style={{ width: '80%' }}
          >
            {loading.effects['hbaseAdmin/create'] ? <Icon type="loading" /> : 'Create'}
          </Button>
        </Form.Item>
      </Form>
    );
  }
}

export default Form.create({ name: 'dynamic_form_item' })(CreateTableDialog);
