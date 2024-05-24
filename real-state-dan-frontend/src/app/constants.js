export const USER_ROLES = {
  ADMIN: 'ADMIN',
  OWNER: 'OWNER',
  CUSTOMER: 'CUSTOMER',
};

export const PROPERTY_STATUS = {
  AVAILABLE: 'AVAILABLE', // can displayed to customer, approved by admin
  PENDING: 'PENDING', // got offer from customer and owner accepted but still needs some verification
  CONTINGENT: 'CONTINGENT', // got offer from customer and owner accepted and verified
  UNAVAILABLE: 'UNAVAILABLE', // new property, not approved by admin
};

export const LISTING_TYPE = {
  SALE: 'SALE',
  RENT: 'RENT',
};

export const PROPERTY_TYPE = {
  APARTMENT: 'APARTMENT',
  HOUSE: 'HOUSE',
  CONDO: 'CONDO',
};

export const OFFER_STATUS = {
  PENDING: 'PENDING',
  APPROVED: 'APPROVED',
  REJECTED: 'REJECTED',
  CANCEL: 'CANCEL',
};

export const ACCESS_TOKEN = 'accessToken';
