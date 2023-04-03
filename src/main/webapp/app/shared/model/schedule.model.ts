export interface ISchedule {
  id?: number;
  faculty?: string | null;
  level?: number | null;
  description?: string | null;
}

export const defaultValue: Readonly<ISchedule> = {};
