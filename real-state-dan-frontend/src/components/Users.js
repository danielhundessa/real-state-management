import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Space, Table } from 'antd';
import { httpGet } from '../api';

function Users() {
  const [users, setUsers] = useState([]);

  const fetchCustomers = async () => {
    const res = await httpGet({
      url: '/users',
    });
    setUsers(res.data || []);
  };

  const columns = [
    {
      title: '#',
      dataIndex: 'id',
      key: 'id',
    },
    {
      title: 'Type',
      dataIndex: 'email',
      key: 'email',
    },
    {
      title: 'Role',
      dataIndex: 'role',
      key: 'role',
    },
    {
      title: 'Properties Size (If Owner)',
      dataIndex: 'action',
      render: (_, record) => <p> {record.properties.length} </p>,
    },
  ];

  useEffect(() => {
    fetchCustomers();
  }, []);

  return (
    <div className="px-4 py-5 sm:px-6">
      <Table columns={columns} dataSource={users} />
    </div>
  );
}

export default Users;
