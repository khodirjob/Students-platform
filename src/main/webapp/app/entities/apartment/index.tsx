import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Apartment from './apartment';
import ApartmentDetail from './apartment-detail';
import ApartmentUpdate from './apartment-update';
import ApartmentDeleteDialog from './apartment-delete-dialog';

const ApartmentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Apartment />} />
    <Route path="new" element={<ApartmentUpdate />} />
    <Route path=":id">
      <Route index element={<ApartmentDetail />} />
      <Route path="edit" element={<ApartmentUpdate />} />
      <Route path="delete" element={<ApartmentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ApartmentRoutes;
