/* eslint-disable new-cap */
import { useEffect, useState } from 'react';
import { Table } from 'antd';
import { useSelector } from 'react-redux';
import jsPDF from 'jspdf';
import { httpGet, httpPut } from '../api';
import { OFFER_STATUS, USER_ROLES } from '../app/constants';
import { notifySuccess } from '../helpers/notification';

function Offers() {
  const auth = useSelector((state) => state.auth);
  const user = auth.user || {};
  const isOwner = USER_ROLES.OWNER === user.role;

  const [offers, setOffers] = useState([]);
  const [flag] = useState(0);

  const fetchOffers = async () => {
    const res = await httpGet({
      url: '/offers',
    });
    setOffers(res.data || []);
  };

  const handleOfferCancel = async (id) => {
    const offer = offers.find((of) => of.id === id);
    httpPut({
      url: `/offers/${offer.id}`,
      data: {
        status: 'CANCELLED',
      },
    }).then(() => {
      fetchOffers();
    });
  };

  const handleContractDownload = async (offerId) => {
    const offer = offers.find((of) => of.id === offerId);
    console.log('handleContractDownload:', offer);
    const doc = new jsPDF();

    let i = 0;
    Object.entries(offer).forEach(([key, value]) => {
      doc.text(`${key}: ${value}`, i * 10, i * 10);
      i += 1;
    });

    doc.save('receipt.pdf');
  };

  const handleOfferOwner = async (id, type) => {
    const status = type === 'accept' ? 'APPROVED' : 'CANCELLED';
    const offer = offers.find((of) => of.id === id);
    httpPut({
      url: `/offers/${offer.id}`,
      data: {
        status,
      },
    }).then((r) => {
      notifySuccess(r.data.message);
      fetchOffers();
    });
  };

  const columns = [
    {
      title: 'Property ID',
      dataIndex: 'propertyId',
      key: 'propertyId',
    },
    {
      title: 'Property Status',
      dataIndex: 'propertyStatus',
      key: 'propertyStatus',
    },
    {
      title: 'Offer ID',
      dataIndex: 'id',
      key: 'id',
    },
    {
      title: 'Offer Status',
      dataIndex: 'offerStatus',
      key: 'offerStatus',
    },
    {
      title: 'Offer Message',
      dataIndex: 'message',
      key: 'message',
    },
    {
      title: 'Offer Price $',
      dataIndex: 'price',
      key: 'price',
    },
    {
      title: 'Offer Action',
      dataIndex: 'offerStatus',
      key: 'offerStatus',
      render: (text, offer) => {
        console.log(text);
        if (text === OFFER_STATUS.APPROVED) {
          return (
            <button
              onClick={() => handleContractDownload(offer.id)}
              className="bg-red-500 text-white px-2"
              type="button"
            >
              Download Receipt
            </button>
          );
        }

        if (isOwner) {
          return (
            <>
              <button
                onClick={() => handleOfferOwner(offer.id, 'accept')}
                className="bg-blue-500 text-white px-2 mr-4"
                type="button"
              >
                Accept
              </button>
              <button
                onClick={() => handleOfferOwner(offer.id, 'reject')}
                className="bg-red-500 text-white px-2"
                type="button"
              >
                Reject
              </button>
            </>
          );
        }

        if (text === OFFER_STATUS.PENDING) {
          return (
            <button
              onClick={() => handleOfferCancel(offer.id)}
              className="bg-red-500 text-white px-2"
              type="button"
            >
              Cancel
            </button>
          );
        }

        return null;
      },
    },
  ];

  useEffect(() => {
    fetchOffers();
  }, [flag]);

  return (
    <div className="px-4 py-5 sm:px-6">
      <Table columns={columns} dataSource={offers.map((o) => ({ ...o, key: o.id }))} />
    </div>
  );
}

export default Offers;
