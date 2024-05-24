import { createSlice } from '@reduxjs/toolkit';

export const propertySlice = createSlice({
  name: 'property',
  initialState: {
    properties: [],
  },
  reducers: {
    findAll: (state, action) => {
      state.properties = action.payload;
    },
  },
});

export const { findAll } = propertySlice.actions;
export default propertySlice.reducer;
