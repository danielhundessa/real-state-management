import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { decrement, increment } from '../store/slices/counter';
import { notifyError, notifySuccess } from '../helpers/notification';

function Test() {
  const counter = useSelector((state) => state.counter.value);
  const dispatch = useDispatch();

  const handleAddClick = () => {
    dispatch(increment());
    notifySuccess('Clicked increment');
  };

  const handleDecrementClick = () => {
    dispatch(decrement());
    notifyError('Clicked decrement');
  };

  return (
    <div>
      <div>Counter: {counter}</div>
      <button onClick={handleAddClick} type="button">
        Plus
      </button>
      <button onClick={handleDecrementClick} type="button">
        Minus
      </button>
    </div>
  );
}

export default Test;
