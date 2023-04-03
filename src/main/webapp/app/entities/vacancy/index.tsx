import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Vacancy from './vacancy';
import VacancyDetail from './vacancy-detail';
import VacancyUpdate from './vacancy-update';
import VacancyDeleteDialog from './vacancy-delete-dialog';

const VacancyRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Vacancy />} />
    <Route path="new" element={<VacancyUpdate />} />
    <Route path=":id">
      <Route index element={<VacancyDetail />} />
      <Route path="edit" element={<VacancyUpdate />} />
      <Route path="delete" element={<VacancyDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VacancyRoutes;
