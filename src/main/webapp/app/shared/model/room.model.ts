export interface IRoom {
  id?: number;
  name?: string | null;
  description?: string | null;
  building?: string | null;
}

export const defaultValue: Readonly<IRoom> = {};
