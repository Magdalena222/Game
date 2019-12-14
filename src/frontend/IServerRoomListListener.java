package frontend;

import backend.logic.Room;

public interface IServerRoomListListener {
    void roomListReceived(Room[] rooms);
}
