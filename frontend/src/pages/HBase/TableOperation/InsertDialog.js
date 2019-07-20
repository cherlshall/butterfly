import React, { PureComponent } from 'react';
import { connect } from 'dva';
import { Form, Input, Icon, Button, Select, Spin, Divider } from 'antd';
import GridContent from '@/components/PageHeaderWrapper/GridContent';
import styles from './TableOperation.less';

const InputGroup = Input.Group;
const { Option, OptGroup } = Select;

let id = 0;

@connect(({ tableOperation, loading }) => ({
  tableOperation,
  loading,
}))
class InsertDialog extends PureComponent {

  state = {
    tmpQualifiers: [],
    currentSearch: '',
  }

  componentDidMount() {
    this.add();
    this.listFamilies(this.props.tableName);
  }

  componentWillReceiveProps(newProps) {
    if (newProps.tableName !== this.props.tableName) {
      this.listFamilies(newProps.tableName);
      this.redo();
    }
  }

  insert = (rowKey, keys, family, qualifier, value) => {
    const beans = [];
    keys.forEach(key => {
      beans.push({
        family: family[key].substring(family[key].indexOf(".") + 1),
        qualifier: qualifier[key].substring(qualifier[key].indexOf(".") + 1),
        value: value[key].substring(value[key].indexOf(".") + 1),
      })
    })
    const { dispatch, tableName, insertOver } = this.props;
    dispatch({
      type: 'tableOperation/insertRow',
      payload: {
        tableName,
        rowKey,
        beans,
      },
      callback: () => insertOver(rowKey),
    });
  }

  listFamilies = (tableName) => {
    if (tableName) {
      const { dispatch } = this.props;
      dispatch({
        type: 'tableOperation/listFamily',
        payload: {
          tableName,
        },
      })
    }
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

  redo = k => {
    const { form } = this.props;
    form.setFieldsValue({
      keys: [id++],
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
        const { rowKey, keys, family, qualifier, value } = values;
        this.insert(rowKey, keys, family, qualifier, value);
      }
    });
  };

  onBlur = (value, k) => {
    const { currentSearch } = this.state;
    if (currentSearch) {
      this.addQualifier(currentSearch);
      this.props.form.setFieldsValue({[`qualifier[${k}]`]: currentSearch})
      this.setState({
        currentSearch: '',
      })
    }
  }

  onSelect = (value, k) => {
    const { currentSearch } = this.state;
    this.setState({
      currentSearch: '',
    })
  }

  addQualifier = (value) => {
    const { tmpQualifiers } = this.state;
    let exist = false;
    for (let i = 0; i < tmpQualifiers.length; i++) {
      if (value === tmpQualifiers[i]) {
        exist = true;
        break;
      }
    }
    if (!exist) {
      tmpQualifiers.push(value);
      this.setState({
        tmpQualifiers,
      })
    }
  }

  fieldHelp = (k, keys) => {
    const { family, qualifier, value } = this.props.form.getFieldsValue();
    if (!family && !qualifier && !value) {
      return;
    }
    let onlyOne = true;
    if (keys && keys.length > 1) {
      onlyOne = false;
    }
    if (family && !family[k] && family[k] !== 0) {
      return onlyOne ? 'Please input family.' : 'Please input family or delete this field.';
    }
    if (qualifier && !qualifier[k] && qualifier[k] !== 0) {
      return onlyOne ? 'Please input qualifier.' : 'Please input qualifier or delete this field.';
    }
    if (value && !value[k] && value[k] !== 0) {
      return onlyOne ? 'Please input value.' : 'Please input value or delete this field.';
    }
    return;
  }

  render() {
    const { tableOperation, loading, tableName } = this.props;
    const { families, familyAndQualifiers } = tableOperation;
    const { getFieldDecorator, getFieldValue } = this.props.form;
    const { tmpQualifiers } = this.state;

    const formItemLayout = {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 4 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 20 },
      },
    };
    const formItemLayoutWithOutLabel = {
      wrapperCol: {
        xs: { span: 24, offset: 0 },
        sm: { span: 20, offset: 4 },
      },
    };
    getFieldDecorator('keys', { initialValue: [] });
    const keys = getFieldValue('keys');
    const formItems = keys.map((k, index) => (
      <Form.Item
        {...(index === 0 ? formItemLayout : formItemLayoutWithOutLabel)}
        label={index === 0 ? 'F/Q/V' : ''}
        required={true}
        key={k}
        help={this.fieldHelp(k, keys)}
        validateStatus={this.fieldHelp(k) ? 'error' : 'success'}
      >
        <InputGroup style={{ width: '90%', marginRight: 8 }} compact>
          {getFieldDecorator(`family[${k}]`, {
            validateTrigger: ['onChange', 'onBlur'],
            rules: [
              {
                required: true,
                whitespace: true,
                message: keys.length === 1 ? "Please input family." : "Please input or delete this field.",
              },
            ],
          })(
            <Select style={{ width: '25%' }} placeholder="family">
              {
                families.map(family => (
                  <Option key={family} value={family}>{family}</Option>
                ))
              }
            </Select>
          )}
          {getFieldDecorator(`qualifier[${k}]`, {
            trigger: ['onSelect', 'onBlur'],
            validateTrigger: ['onSelect'],
            rules: [
              {
                required: true,
                whitespace: true,
                message: keys.length === 1 ? "Please input qualifier." : "Please input or delete this field.",
              },
            ],
          })(
            <Select 
              style={{ width: '35%' }} 
              placeholder="qualifier"
              showSearch
              onSearch={value => {
                this.setState({currentSearch: value})
              }}
              onBlur={value => this.onBlur(value, k)}
              onSelect={value => this.onSelect(value, k)}
              filterOption={() => true}
            >
              {tmpQualifiers.length > 0 && 
                <OptGroup key="tmp_add_qua" label="new addition">
                  {tmpQualifiers.map(qua => {
                    return (
                    <Option key={`tmp_add_qua.${qua}`} value={`tmp_add_qua.${qua}`}>{qua}</Option>
                  )})}
                </OptGroup>
              }
              {familyAndQualifiers.map(faq => (
                <OptGroup key={faq.family} label={faq.family}>
                  {faq.qualifiers.map(qua => (
                    <Option key={`${faq.family}.${qua}`} value={`${faq.family}.${qua}`}>{qua}</Option>
                  ))}
                </OptGroup>
              ))}
            </Select>
          )}
          
          {getFieldDecorator(`value[${k}]`, {
            validateTrigger: ['onChange', 'onBlur'],
            rules: [
              {
                required: true,
                whitespace: true,
                message: keys.length === 1 ? "Please input value." : "Please input or delete this field.",
              },
            ],
          })(
            <Input style={{ width: '40%' }} placeholder="value" />
          )}
        </InputGroup>
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
      <Spin spinning={!!loading.effects["tableOperation/listFamily"]}>
        <Form onSubmit={this.handleSubmit}>
          <Form.Item
            {...formItemLayout}
            label={'rowKey'}
            required={true}
            key={'rowKey'}
          >
            {getFieldDecorator('rowKey', {
              validateTrigger: ['onChange', 'onBlur'],
              rules: [
                {
                  required: true,
                  whitespace: true,
                  message: "Please input rowKey.",
                },
              ],
            })(<Input placeholder="rowKey" style={{ width: '90%', marginRight: 8 }} />)}
          </Form.Item>
          {formItems}
          <Form.Item {...formItemLayoutWithOutLabel}>
            <Button type="dashed" onClick={this.add} style={{ width: '90%' }}>
              <Icon type="plus" /> Add Field
            </Button>
          </Form.Item>
          <Form.Item {...formItemLayoutWithOutLabel}>
            <Button type="primary" disabled={loading.effects["tableOperation/insertRow"]} htmlType="submit" style={{ width: '90%' }}>
              {loading.effects["tableOperation/insertRow"] ? <Icon type="loading" /> : "Insert"}
            </Button>
          </Form.Item>
        </Form>
      </Spin>
    );
  }
}

export default Form.create({ name: 'dynamic_form_item' })(InsertDialog);
