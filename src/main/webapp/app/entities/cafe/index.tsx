import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Cafe from './cafe';
import CafeDetail from './cafe-detail';
import CafeUpdate from './cafe-update';
import CafeDeleteDialog from './cafe-delete-dialog';

const CafeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Cafe />} />
    <Route path="new" element={<CafeUpdate />} />
    <Route path=":id">
      <Route index element={<CafeDetail />} />
      <Route path="edit" element={<CafeUpdate />} />
      <Route path="delete" element={<CafeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CafeRoutes;
