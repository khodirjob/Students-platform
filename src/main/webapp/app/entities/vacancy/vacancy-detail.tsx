import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './vacancy.reducer';

export const VacancyDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const vacancyEntity = useAppSelector(state => state.vacancy.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vacancyDetailsHeading">
          <Translate contentKey="studentsPlatformApp.vacancy.detail.title">Vacancy</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{vacancyEntity.id}</dd>
          <dt>
            <span id="companyName">
              <Translate contentKey="studentsPlatformApp.vacancy.companyName">Company Name</Translate>
            </span>
          </dt>
          <dd>{vacancyEntity.companyName}</dd>
          <dt>
            <span id="jobType">
              <Translate contentKey="studentsPlatformApp.vacancy.jobType">Job Type</Translate>
            </span>
          </dt>
          <dd>{vacancyEntity.jobType}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="studentsPlatformApp.vacancy.description">Description</Translate>
            </span>
          </dt>
          <dd>{vacancyEntity.description}</dd>
          <dt>
            <span id="vacancyCount">
              <Translate contentKey="studentsPlatformApp.vacancy.vacancyCount">Vacancy Count</Translate>
            </span>
          </dt>
          <dd>{vacancyEntity.vacancyCount}</dd>
          <dt>
            <span id="location">
              <Translate contentKey="studentsPlatformApp.vacancy.location">Location</Translate>
            </span>
          </dt>
          <dd>{vacancyEntity.location}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="studentsPlatformApp.vacancy.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{vacancyEntity.phone}</dd>
          <dt>
            <span id="salary">
              <Translate contentKey="studentsPlatformApp.vacancy.salary">Salary</Translate>
            </span>
          </dt>
          <dd>{vacancyEntity.salary}</dd>
        </dl>
        <Button tag={Link} to="/vacancy" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vacancy/${vacancyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VacancyDetail;
