import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './authors.reducer';

export const AuthorsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const authorsEntity = useAppSelector(state => state.authors.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="authorsDetailsHeading">
          <Translate contentKey="studentsPlatformApp.authors.detail.title">Authors</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{authorsEntity.id}</dd>
          <dt>
            <span id="fullName">
              <Translate contentKey="studentsPlatformApp.authors.fullName">Full Name</Translate>
            </span>
          </dt>
          <dd>{authorsEntity.fullName}</dd>
          <dt>
            <span id="birthDay">
              <Translate contentKey="studentsPlatformApp.authors.birthDay">Birth Day</Translate>
            </span>
          </dt>
          <dd>{authorsEntity.birthDay ? <TextFormat value={authorsEntity.birthDay} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="dedDay">
              <Translate contentKey="studentsPlatformApp.authors.dedDay">Ded Day</Translate>
            </span>
          </dt>
          <dd>{authorsEntity.dedDay ? <TextFormat value={authorsEntity.dedDay} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/authors" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/authors/${authorsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AuthorsDetail;
