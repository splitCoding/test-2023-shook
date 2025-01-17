import { createContext, useEffect, useState } from 'react';
import useVideoPlayerContext from '@/features/youtube/hooks/useVideoPlayerContext';
import type { KillingPartInterval } from '../types/KillingPartToggleGroup.type';
import type { PropsWithChildren } from 'react';

interface VoteInterfaceContextProps extends VoteInterfaceProviderProps {
  partStartTime: number;
  interval: KillingPartInterval;
  // NOTE: Why both setState and eventHandler have same naming convention?
  updatePartStartTime: (timeUnit: string, value: number) => void;
  updateKillingPartInterval: React.MouseEventHandler<HTMLButtonElement>;
}

export const VoteInterfaceContext = createContext<VoteInterfaceContextProps | null>(null);

interface VoteInterfaceProviderProps {
  videoLength: number;
  songId: number;
}

export const VoteInterfaceProvider = ({
  children,
  videoLength,
  songId,
}: PropsWithChildren<VoteInterfaceProviderProps>) => {
  const [interval, setInterval] = useState<KillingPartInterval>(10);
  const [partStartTime, setPartStartTime] = useState(0);
  const { videoPlayer } = useVideoPlayerContext();

  const updateKillingPartInterval: React.MouseEventHandler<HTMLButtonElement> = (e) => {
    const newInterval = Number(e.currentTarget.dataset['interval']) as KillingPartInterval;
    const partEndTime = partStartTime + newInterval;

    if (partEndTime > videoLength) {
      const overflowedSeconds = partEndTime - videoLength;
      setPartStartTime(partStartTime - overflowedSeconds);
    }

    setInterval(newInterval);
  };

  const updatePartStartTime = (timeUnit: string, value: number) => {
    if (timeUnit === 'minute') {
      setPartStartTime((prev) => {
        const minute = 60 * value;
        const second = prev % 60;

        return minute + second;
      });
    }

    if (timeUnit === 'second') {
      setPartStartTime((prev) => {
        const minute = 60 * Math.floor(prev / 60);
        const second = value;

        return minute + second;
      });
    }
  };

  useEffect(() => {
    const timer = window.setInterval(() => {
      videoPlayer?.seekTo(partStartTime, true);
    }, interval * 1000);

    return () => window.clearInterval(timer);
  }, [videoPlayer, partStartTime, interval]);

  return (
    <VoteInterfaceContext.Provider
      value={{
        partStartTime,
        interval,
        videoLength,
        songId,
        updatePartStartTime,
        updateKillingPartInterval,
      }}
    >
      {children}
    </VoteInterfaceContext.Provider>
  );
};
