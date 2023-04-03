import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Authors from './authors';
import AuthorsDetail from './authors-detail';
import AuthorsUpdate from './authors-update';
import AuthorsDeleteDialog from './authors-delete-dialog';

const AuthorsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Authors />} />
    <Route path="new" element={<AuthorsUpdate />} />
    <Route path=":id">
      <Route index element={<AuthorsDetail />} />
      <Route path="edit" element={<AuthorsUpdate />} />
      <Route path="delete" element={<AuthorsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AuthorsRoutes;
