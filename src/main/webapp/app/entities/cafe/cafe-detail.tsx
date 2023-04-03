import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cafe.reducer';

export const CafeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const cafeEntity = useAppSelector(state => state.cafe.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cafeDetailsHeading">
          <Translate contentKey="studentsPlatformApp.cafe.detail.title">Cafe</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cafeEntity.id}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="studentsPlatformApp.cafe.description">Description</Translate>
            </span>
          </dt>
          <dd>{cafeEntity.description}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="studentsPlatformApp.cafe.name">Name</Translate>
            </span>
          </dt>
          <dd>{cafeEntity.name}</dd>
          <dt>
            <span id="location">
              <Translate contentKey="studentsPlatformApp.cafe.location">Location</Translate>
            </span>
          </dt>
          <dd>{cafeEntity.location}</dd>
          <dt>
            <span id="openingTime">
              <Translate contentKey="studentsPlatformApp.cafe.openingTime">Opening Time</Translate>
            </span>
          </dt>
          <dd>{cafeEntity.openingTime ? <TextFormat value={cafeEntity.openingTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="closeTime">
              <Translate contentKey="studentsPlatformApp.cafe.closeTime">Close Time</Translate>
            </span>
          </dt>
          <dd>{cafeEntity.closeTime ? <TextFormat value={cafeEntity.closeTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/cafe" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cafe/${cafeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CafeDetail;
