import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Book from './book';
import Authors from './authors';
import Category from './category';
import Vacancy from './vacancy';
import Cafe from './cafe';
import Attachment from './attachment';
import Food from './food';
import Room from './room';
import Apartment from './apartment';
import Schedule from './schedule';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="book/*" element={<Book />} />
        <Route path="authors/*" element={<Authors />} />
        <Route path="category/*" element={<Category />} />
        <Route path="vacancy/*" element={<Vacancy />} />
        <Route path="cafe/*" element={<Cafe />} />
        <Route path="attachment/*" element={<Attachment />} />
        <Route path="food/*" element={<Food />} />
        <Route path="room/*" element={<Room />} />
        <Route path="apartment/*" element={<Apartment />} />
        <Route path="schedule/*" element={<Schedule />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
