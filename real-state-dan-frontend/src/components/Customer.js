function Customer(props) {
  return (
    <div className="bg-gray-200 flex flex-col">
      <h1 className="text-xl font-bold">{props.firsName}</h1>
      <p className="text-base">{props.lastName}</p>
      <p className="text-base font-bold">${props.email}</p>
    </div>
  );
}

export default Customer;
