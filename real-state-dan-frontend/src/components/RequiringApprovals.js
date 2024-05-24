/* eslint-disable object-curly-newline */
import { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { Table, Space, Button, Modal } from 'antd';
import { httpGet, httpPut } from '../api';
import { PROPERTY_STATUS, USER_ROLES } from '../app/constants';
import { notifyError, notifySuccess } from '../helpers/notification';

function RequiringApprovals() {
  const auth = useSelector((state) => state.auth);
  const user = auth.user || {};
  const isAdmin = USER_ROLES.ADMIN === user.role;

  const [isModalOpen, setIsModalOpen] = useState(false);
  const showModal = () => {
    setIsModalOpen(true);
  };
  const handleCancel = () => {
    setIsModalOpen(false);
  };

  const [unavailableProperties, setUnavailableProperties] = useState([]);
  const [flag, setFlag] = useState(0);

  const fetchUnavailableProperties = async () => {
    try {
      const res = await httpGet({
        url: `properties?property_status=${PROPERTY_STATUS.UNAVAILABLE}`,
      });
      if (res.data) {
        console.log('Fetched:', res.data);
        setUnavailableProperties(res.data.map((property) => ({ ...property, key: property.id })));
      }
      notifySuccess('Fetched successfully');
    } catch (err) {
      notifyError('Failed to fetch from system');
      console.log(err);
    }
  };

  const handleApprove = (unavailableProperty) => async () => {
    console.log('handleApprove', unavailableProperty);
    try {
      const res = await httpPut({
        url: `properties/${unavailableProperty.id}/publish`,
      });
      if (res.data) {
        console.log('Approved:', res.data);
        notifySuccess('Approved successfully');
        setIsModalOpen(false);
        setFlag(flag + 1);
      }
    } catch (err) {
      notifyError('Failed to approve');
      console.log(err);
    }
  };

  const columns = [
    {
      title: 'Propert Id',
      dataIndex: 'id',
      key: 'id',
    },
    {
      title: 'title',
      dataIndex: 'title',
      key: 'title',
    },
    {
      title: 'Property Status',
      dataIndex: 'propertyStatus',
      key: 'propertyStatus',
    },
    {
      title: 'Owner Id',
      dataIndex: 'ownerId',
      key: 'ownerId',
    },
    {
      title: 'Action',
      key: 'button',
      render: (_, record) => (
        <Space size="middle" key={record.id}>
          <Button onClick={showModal} type="default">
            Approve {record.name}
          </Button>

          <Modal
            title="Approving this Property?"
            open={isModalOpen}
            onOk={handleApprove(record)}
            onCancel={handleCancel}
            okType="danger"
            okText="Approve"
          />
        </Space>
      ),
    },
  ];

  useEffect(() => {
    fetchUnavailableProperties();
  }, [flag]);

  return (
    <div>
      {isAdmin && (
        <div className="px-4 py-5 sm:px-6">
          <h3 className="text-lg font-medium leading-6 text-gray-900">
            Recent Requiring Approvals
          </h3>
          <div className="px-4 py-5 sm:px-6">
            <Table columns={columns} dataSource={unavailableProperties} />
          </div>
        </div>
      )}
    </div>
  );
}

export default RequiringApprovals;
