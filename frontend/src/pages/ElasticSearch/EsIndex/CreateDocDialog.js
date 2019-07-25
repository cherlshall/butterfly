import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Form, Input, Icon, Button, Modal } from 'antd';
import GridContent from '@/components/PageHeaderWrapper/GridContent';

@connect(({ esIndex, loading }) => ({
  esIndex,
  loading,
}))
class CreateDocDialog extends PureComponent {

  create = (indexName, values) => {
    const { dispatch } = this.props;
    dispatch({
      type: 'esIndex/createDoc',
      payload: {
        indexName,
        bean: values,
      },
      callback: () => {
        this.props.closeDialog();
        if (this.props.createOver) {
          this.props.createOver();
        }
      }
    });
    
  }

  handleSubmit = e => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
        this.create(this.props.indexName, values);
      }
    });
  };

  render() {
    const { properties, loading, visible, closeDialog, indexName } = this.props;
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
    const formItems = properties.map(item => (
      <Form.Item
        {...formItemLayout}
        label={item}
        required={false}
        key={item}
      >
        {getFieldDecorator(item, {
          validateTrigger: ['onChange', 'onBlur'],
          rules: [
            {
              required: false,
              whitespace: true,
              message: "Please check value.",
            },
          ],
        })(<Input placeholder="property value" style={{ width: '80%', marginRight: 8 }} />)}
      </Form.Item>
    ));
    return (
      <Modal
        title={`Insert to ${indexName}`}
        visible={visible}
        onCancel={closeDialog}
        footer={null}
      >
        <Form onSubmit={this.handleSubmit}>
          <Form.Item
            {...formItemLayout}
            label={'_id'}
            required={false}
            key={'_id'}
          >
            {getFieldDecorator('_id', {
              validateTrigger: ['onChange', 'onBlur'],
              rules: [
                {
                  required: false,
                  whitespace: true,
                  message: "Please check value.",
                },
              ],
            })(<Input placeholder="ElasticSearch id, optionally" style={{ width: '80%', marginRight: 8 }} />)}
          </Form.Item>
          {formItems}
          <Form.Item {...formItemLayoutWithOutLabel}>
            <Button type="primary" disabled={loading.effects["esIndex/createDoc"]} htmlType="submit" style={{ width: '80%' }}>
              {loading.effects["esIndex/createDoc"] ? <Icon type="loading" /> : "Insert"}
            </Button>
          </Form.Item>
        </Form>
      </Modal>
    );
  }
}

export default Form.create({ name: 'es_create_doc_form' })(CreateDocDialog);
