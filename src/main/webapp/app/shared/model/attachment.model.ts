import { AttachmentType } from 'app/shared/model/enumerations/attachment-type.model';

export interface IAttachment {
  id?: number;
  name?: string | null;
  fileOriginalName?: string | null;
  attachSize?: number | null;
  contentType?: string | null;
  attachmentType?: AttachmentType;
  objectId?: number | null;
}

export const defaultValue: Readonly<IAttachment> = {};
