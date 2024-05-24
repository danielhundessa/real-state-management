/* eslint-disable indent */
/* eslint-disable operator-linebreak */
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { Button, Modal } from 'antd';
import { PROPERTY_STATUS, USER_ROLES } from '../app/constants';
import { httpDelete, httpGet, httpPut } from '../api';
import { notifyError, notifySuccess } from '../helpers/notification';
import Property from './Property';

function PropertiesOwner() {
  const navigate = useNavigate();

  const auth = useSelector((state) => state.auth);
  const user = auth.user || {};
  const isOwner = USER_ROLES.OWNER === user.role;

  const [properties, setProperties] = useState([]);

  const [flag, setFlag] = useState(0);

  const [isModalDeleteOpen, setIsModalDeleteOpen] = useState(false);
  const [isModalCancelContingentOpen, setIsModalCancelContingentOpen] = useState(false);
  const [isModalVerifyOpen, setIsModalVerifyOpen] = useState(false);

  const showModalDelete = () => {
    setIsModalDeleteOpen(true);
  };
  const handleDeleteCancel = () => {
    setIsModalDeleteOpen(false);
  };
  const showModalCancelContingent = () => {
    setIsModalCancelContingentOpen(true);
  };
  const handleCancelContingentCancel = () => {
    setIsModalCancelContingentOpen(false);
  };
  const showModalVerify = () => {
    setIsModalVerifyOpen(true);
  };
  const handleVerifyCancel = () => {
    setIsModalVerifyOpen(false);
  };

  const handleDelete = (propertyId) => async () => {
    console.log('handleDelete:', propertyId);
    try {
      const res = await httpDelete({
        url: `properties/${propertyId}`,
      });
      if (res.data) {
        console.log('Deleted:', res.data);
        notifySuccess('Deleted successfully');
        setIsModalDeleteOpen(false);
        setFlag(flag + 1);
      }
    } catch (err) {
      notifyError('Failed to delete');
      console.log(err);
    }
  };

  const handleCancelContingent = (pId) => async () => {
    console.log('handleCancelContingent:', pId);
    try {
      const res = await httpPut({
        url: `properties/${pId}/cancel-contingent`,
      });
      if (res.data) {
        console.log('Updated:', res.data);
        notifySuccess('Updated Contingent to Available successfully');
        setIsModalCancelContingentOpen(false);
        setFlag(flag + 1);
      }
    } catch (err) {
      notifyError('Failed to Updated Contingent to Available');
      console.log(err);
    }
  };
  const handleVerify = (pId) => async () => {
    console.log('handleVerify:', pId);
    try {
      const res = await httpPut({
        url: `properties/${pId}/contingent`,
      });
      if (res.data) {
        console.log('Updated Verify:', res.data);
        notifySuccess('Updated Pending to Contingent successfully');
        setIsModalVerifyOpen(false);
        setFlag(flag + 1);
      }
    } catch (err) {
      notifyError('Failed to Updated Pending to Contingent');
      console.log(err);
    }
  };

  const fetchProperties = async () => {
    if (isOwner) {
      try {
        const res = await httpGet({
          url: `/properties?owner_id=${user.userId}`,
        });
        if (res.data) setProperties(res.data);
      } catch (err) {
        notifyError('Failed to fetch from system');
        console.log(err);
      }
    }
  };

  const handleEdit = (id) => {
    navigate(`/properties/${id}/edit`);
  };

  useEffect(() => {
    fetchProperties();
  }, [user, flag]);

  return (
    <div>
      <div className="bg-white">
        <div className="mx-auto max-w-2xl py-16 px-4 sm:py-24 sm:px-6 lg:max-w-7xl lg:px-8">
          <h2 className="sr-only">ps</h2>

          <div className="grid grid-cols-1 gap-y-10 gap-x-6 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 xl:gap-x-8">
            {properties.map((p) => (
              <div key={p.id}>
                <Property key={p.id} p={p} />
                {(p.propertyStatus === PROPERTY_STATUS.AVAILABLE ||
                  p.propertyStatus === PROPERTY_STATUS.UNAVAILABLE) && (
                  <div>
                    <Button onClick={() => handleEdit(p.id)} type="default">
                      Edit
                    </Button>
                    <Button onClick={showModalDelete} type="default">
                      Delete
                    </Button>{' '}
                    <Modal
                      title={`Deleting this "${p.title}" ?`}
                      open={isModalDeleteOpen}
                      onOk={handleDelete(p.id)}
                      onCancel={handleDeleteCancel}
                      okType="danger"
                      okText="Delete"
                    />
                  </div>
                )}
                {p.propertyStatus === PROPERTY_STATUS.CONTINGENT && (
                  <div>
                    <Button onClick={showModalCancelContingent} type="default">
                      Cancel Contingent
                    </Button>{' '}
                    <Modal
                      title={`Cancelling this contingent "${p.title}" ?`}
                      open={isModalCancelContingentOpen}
                      onOk={handleCancelContingent(p.id)}
                      onCancel={handleCancelContingentCancel}
                      okType="danger"
                      okText="Cancel Contingent"
                    />
                  </div>
                )}
                {p.propertyStatus === PROPERTY_STATUS.PENDING && (
                  <div>
                    <Button onClick={showModalVerify} type="default">
                      Verify
                    </Button>{' '}
                    <Modal
                      title={`Verify this "${p.title}" to contingent?`}
                      open={isModalVerifyOpen}
                      onOk={handleVerify(p.id)}
                      onCancel={handleVerifyCancel}
                      okType="danger"
                      okText="Verify"
                    />
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}

export default PropertiesOwner;
