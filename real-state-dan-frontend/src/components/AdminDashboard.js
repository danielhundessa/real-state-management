import Users from './Users';
import Properties from './Properties';

function AdminDashboard() {
  const propertyMaxNum = 2;
  const propertyStatuses = ['contingent'];
  const usersMaxNum = 2;
  const userRoles = ['customer'];

  return (
    <div>
      <div className="px-4 py-5 sm:px-6">
        <h3 className="text-lg font-medium leading-6 text-gray-900">
          Recent Contingent Properties
        </h3>
        <p className="mt-1 max-w-2xl text-sm text-gray-500">Displaying latest {propertyMaxNum}</p>
      </div>
      <Properties propertyMaxNum={propertyMaxNum} propertyStatuses={propertyStatuses} />
      <div className="px-4 py-5 sm:px-6">
        <h3 className="text-lg font-medium leading-6 text-gray-900">Recent Customers</h3>
        <p className="mt-1 max-w-2xl text-sm text-gray-500">Displaying latest {usersMaxNum}</p>
      </div>
      <Users usersMaxNum={usersMaxNum} userRoles={userRoles} />
    </div>
  );
}

export default AdminDashboard;
