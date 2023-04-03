import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './food.reducer';

export const FoodDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const foodEntity = useAppSelector(state => state.food.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="foodDetailsHeading">
          <Translate contentKey="studentsPlatformApp.food.detail.title">Food</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{foodEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="studentsPlatformApp.food.name">Name</Translate>
            </span>
          </dt>
          <dd>{foodEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="studentsPlatformApp.food.description">Description</Translate>
            </span>
          </dt>
          <dd>{foodEntity.description}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="studentsPlatformApp.food.price">Price</Translate>
            </span>
          </dt>
          <dd>{foodEntity.price}</dd>
        </dl>
        <Button tag={Link} to="/food" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/food/${foodEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FoodDetail;
