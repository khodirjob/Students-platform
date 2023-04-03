import dayjs from 'dayjs';

export interface ICafe {
  id?: number;
  description?: string | null;
  name?: string | null;
  location?: string | null;
  openingTime?: string | null;
  closeTime?: string | null;
}

export const defaultValue: Readonly<ICafe> = {};
