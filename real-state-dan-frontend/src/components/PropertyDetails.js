import { Button, Modal } from 'antd';
import { useState, useEffect, useRef } from 'react';
import { useParams } from 'react-router';
import { useSelector } from 'react-redux';
import { httpGet, httpPost } from '../api';
import { notifyError, notifySuccess } from '../helpers/notification';
import { USER_ROLES } from '../app/constants';

function PropertyDetails() {
  const auth = useSelector((state) => state.auth);
  const user = auth.user || {};

  const params = useParams();
  const [property, setProperty] = useState({});
  const [formOfferData, setFormOfferData] = useState({
    price: '',
    message: '',
  });
  const [formInquiryData, setFormInquiryData] = useState({
    message: '',
  });
  const offerRef = useRef();
  const inquiryRef = useRef();
  const isCustomer = USER_ROLES.CUSTOMER === user.role;

  const fetchProperty = async () => {
    const res = await httpGet({
      url: `/properties/${params.id}`,
    });
    setProperty(res.data);
  };

  const [isOfferOpen, setIsOfferOpen] = useState(false);
  const [isInquiryOpen, setIsInquiryOpen] = useState(false);
  const showOffer = () => {
    setIsOfferOpen(true);
  };
  const showInquiry = () => {
    setIsInquiryOpen(true);
  };
  const handleOffer = async () => {
    const res = await httpPost({
      url: `/properties/${params.id}/offer`,
      data: formOfferData,
    });
    if (res.data.status === 'CREATED') {
      notifySuccess('Offer sent!');
    } else {
      notifyError(res.data.message);
    }
    setIsOfferOpen(false);
  };
  const handleCancelOffer = () => {
    setIsOfferOpen(false);
  };
  const handleInquiry = async () => {
    const res = await httpPost({
      url: `/properties/${params.id}/inquiry`,
      data: formInquiryData,
    });
    if (res.data.status === 'CREATED') {
      notifySuccess('Inquiry sent!');
    } else {
      notifyError(res.data.message);
    }
    setIsInquiryOpen(false);
  };
  const handleCancelInquiry = () => {
    setIsInquiryOpen(false);
  };

  const handleInputOfferChange = (e) => {
    setFormOfferData({
      ...formOfferData,
      [e.target.name]: e.target.value,
    });
  };

  const handleInputInquiryChange = (e) => {
    setFormInquiryData({
      ...formInquiryData,
      [e.target.name]: e.target.value,
    });
  };

  useEffect(() => {
    fetchProperty();
  }, [params.id]);

  const p = property;

  return (
    <div>
      <div className="p-5 text-center">
        <h1>Property Details</h1>
        <h2>{property.title}</h2>
        <p>{property.description}</p>
        <p>${property.price}</p>
        <p>listingType: {p.listingType}</p>
        <p>propertyStatus: {p.propertyStatus}</p>
        <p>
          Location: {p.address} {p.city} {p.state}, {p.zipCode}
        </p>
        <img className="block mx-auto w-1/4" src={property.image} alt={property.title} />
      </div>
      {user?.email && isCustomer && (
        <div className="flex justify-center">
          <Button onClick={showOffer} type="default">
            Make Offer?
          </Button>
          <Modal
            title="Make an Offer to the Seller"
            open={isOfferOpen}
            onOk={handleOffer}
            onCancel={handleCancelOffer}
            okType="danger"
            okText="Send Offer"
          >
            <form ref={offerRef} className="flex flex-col">
              <input
                type="text"
                onChange={handleInputOfferChange}
                value={formOfferData.message}
                name="message"
                placeholder="Message"
                required
              />
              <input
                type="number"
                onChange={handleInputOfferChange}
                value={formOfferData.price}
                name="price"
                placeholder="Insert Price"
                required
              />
            </form>
          </Modal>
          <Button onClick={showInquiry} type="default">
            Make Inquiry?
          </Button>
          <Modal
            title="Make an Inquiry to the Seller"
            open={isInquiryOpen}
            onOk={handleInquiry}
            onCancel={handleCancelInquiry}
            okType="danger"
            okText="Send Inquiry"
          >
            <form ref={inquiryRef} className="flex flex-col">
              <input
                type="text"
                onChange={handleInputInquiryChange}
                value={formInquiryData.message}
                name="message"
                placeholder="Message"
                required
              />
            </form>
          </Modal>
        </div>
      )}
    </div>
  );
}

export default PropertyDetails;
