import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './attachment.reducer';

export const AttachmentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const attachmentEntity = useAppSelector(state => state.attachment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="attachmentDetailsHeading">
          <Translate contentKey="studentsPlatformApp.attachment.detail.title">Attachment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{attachmentEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="studentsPlatformApp.attachment.name">Name</Translate>
            </span>
          </dt>
          <dd>{attachmentEntity.name}</dd>
          <dt>
            <span id="fileOriginalName">
              <Translate contentKey="studentsPlatformApp.attachment.fileOriginalName">File Original Name</Translate>
            </span>
          </dt>
          <dd>{attachmentEntity.fileOriginalName}</dd>
          <dt>
            <span id="attachSize">
              <Translate contentKey="studentsPlatformApp.attachment.attachSize">Attach Size</Translate>
            </span>
          </dt>
          <dd>{attachmentEntity.attachSize}</dd>
          <dt>
            <span id="contentType">
              <Translate contentKey="studentsPlatformApp.attachment.contentType">Content Type</Translate>
            </span>
          </dt>
          <dd>{attachmentEntity.contentType}</dd>
          <dt>
            <span id="attachmentType">
              <Translate contentKey="studentsPlatformApp.attachment.attachmentType">Attachment Type</Translate>
            </span>
          </dt>
          <dd>{attachmentEntity.attachmentType}</dd>
          <dt>
            <span id="objectId">
              <Translate contentKey="studentsPlatformApp.attachment.objectId">Object Id</Translate>
            </span>
          </dt>
          <dd>{attachmentEntity.objectId}</dd>
        </dl>
        <Button tag={Link} to="/attachment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/attachment/${attachmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AttachmentDetail;
