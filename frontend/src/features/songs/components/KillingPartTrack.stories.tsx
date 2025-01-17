import { useState } from 'react';
import { VideoPlayerProvider } from '@/features/youtube/components/VideoPlayerProvider';
import ToastProvider from '@/shared/components/Toast/ToastProvider';
import KillingPartTrack from './KillingPartTrack';
import type { KillingPart } from '@/shared/types/song';
import type { Meta, StoryObj } from '@storybook/react';

const meta = {
  component: KillingPartTrack,
  title: 'KillingPartTrack',
  decorators: [
    (Story) => {
      return (
        <ToastProvider>
          <VideoPlayerProvider>
            <Story />
          </VideoPlayerProvider>
        </ToastProvider>
      );
    },
  ],
} satisfies Meta<typeof KillingPartTrack>;

export default meta;
type Story = StoryObj<typeof KillingPartTrack>;

const killingPart: KillingPart = {
  exist: true,
  id: 1,
  rank: 1,
  voteCount: 0,
  start: 70,
  end: 80,
  partVideoUrl: 'https://youtu.be/ArmDp-zijuc?start=105&end=115',
  likeCount: 12,
};

const KillingPartTrackWithHook = () => {
  const [nowPlayingTrack, setNowPlayingTrack] = useState(-1);

  const changePlayingTrack: React.ChangeEventHandler<HTMLInputElement> = ({ currentTarget }) => {
    const newTrack = Number(currentTarget.value);

    setNowPlayingTrack(newTrack);
  };

  const isPlaying = killingPart.rank === nowPlayingTrack;

  return (
    <KillingPartTrack
      killingPart={killingPart}
      isPlaying={isPlaying}
      changePlayingTrack={changePlayingTrack}
    />
  );
};

export const Default: Story = {
  render: () => <KillingPartTrackWithHook />,
};
