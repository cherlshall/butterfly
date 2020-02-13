import React, { Fragment } from 'react';
import { Layout, Icon } from 'antd';
import GlobalFooter from '@/components/GlobalFooter';

const { Footer } = Layout;
const FooterView = () => (
  <Footer style={{ padding: 0 }}>
    <GlobalFooter
      links={[
        {
          key: '主页',
          title: '主页',
          href: 'http://188.131.238.102',
          blankTarget: true,
        },
        {
          key: 'github',
          title: <Icon type="github" />,
          href: 'https://github.com/atparting/butterfly',
          blankTarget: true,
        },
        {
          key: 'CSDN',
          title: 'CSDN',
          href: 'https://blog.csdn.net/cherlshall',
          blankTarget: true,
        },
      ]}
      copyright={
        <Fragment>
          Copyright <Icon type="copyright" /> 2019 cherlshall.com
        </Fragment>
      }
    />
  </Footer>
);
export default FooterView;
