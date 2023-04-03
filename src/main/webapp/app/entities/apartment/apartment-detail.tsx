import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './apartment.reducer';

export const ApartmentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const apartmentEntity = useAppSelector(state => state.apartment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="apartmentDetailsHeading">
          <Translate contentKey="studentsPlatformApp.apartment.detail.title">Apartment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{apartmentEntity.id}</dd>
          <dt>
            <span id="location">
              <Translate contentKey="studentsPlatformApp.apartment.location">Location</Translate>
            </span>
          </dt>
          <dd>{apartmentEntity.location}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="studentsPlatformApp.apartment.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{apartmentEntity.phone}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="studentsPlatformApp.apartment.description">Description</Translate>
            </span>
          </dt>
          <dd>{apartmentEntity.description}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="studentsPlatformApp.apartment.price">Price</Translate>
            </span>
          </dt>
          <dd>{apartmentEntity.price}</dd>
          <dt>
            <span id="requirements">
              <Translate contentKey="studentsPlatformApp.apartment.requirements">Requirements</Translate>
            </span>
          </dt>
          <dd>{apartmentEntity.requirements}</dd>
          <dt>
            <span id="roomFit">
              <Translate contentKey="studentsPlatformApp.apartment.roomFit">Room Fit</Translate>
            </span>
          </dt>
          <dd>{apartmentEntity.roomFit}</dd>
        </dl>
        <Button tag={Link} to="/apartment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/apartment/${apartmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ApartmentDetail;
