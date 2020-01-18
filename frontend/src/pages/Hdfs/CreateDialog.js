import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Form, Input, Icon, Button } from 'antd';

@connect(({ hdfs, loading }) => ({
  hdfs,
  loading,
}))
class CreateDialog extends PureComponent {
  create = name => {
    const { path, onOver } = this.props;
    const { dispatch } = this.props;
    dispatch({
      type: 'hdfs/create',
      payload: {
        path: `${path}${path === '/' ? '' : '/'}${name}`,
      },
      callback: () => onOver(),
    });
  };

  handleSubmit = e => {
    e.preventDefault();
    const { form } = this.props;
    form.validateFields((err, values) => {
      if (!err) {
        const { name } = values;
        this.create(name);
      }
    });
  };

  render() {
    const { loading, path } = this.props;
    const { form } = this.props;
    const { getFieldDecorator, getFieldValue } = form;
    const formItemLayoutSubmit = {
      wrapperCol: {
        xs: { span: 24, offset: 0 },
        sm: { span: 6, offset: 18 },
      },
    };
    const formItemLayoutWithOutLabel = {
      wrapperCol: {
        xs: { span: 24, offset: 0 },
        sm: { span: 24, offset: 0 },
      },
    };
    return (
      <Form onSubmit={this.handleSubmit}>
        <Form.Item {...formItemLayoutWithOutLabel} required>
          {getFieldDecorator('name', {
            validateTrigger: ['onChange', 'onBlur'],
            rules: [
              {
                required: true,
                whitespace: true,
                message: 'Please input file name.',
              },
            ],
          })(<Input addonBefore={path} placeholder="file name" />)}
        </Form.Item>
        <Form.Item {...formItemLayoutSubmit}>
          <Button
            type="primary"
            disabled={loading.effects['hdfs/create']}
            htmlType="submit"
            style={{ width: '100%' }}
          >
            {loading.effects['hdfs/create'] ? <Icon type="loading" /> : 'Create'}
          </Button>
        </Form.Item>
      </Form>
    );
  }
}

export default Form.create({ name: 'hdfs_create_dialog' })(CreateDialog);
