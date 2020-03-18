package click.dobel.alexasonic.restclient;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.subsonic.restapi.ArtistID3;
import org.subsonic.restapi.ArtistsID3;
import org.subsonic.restapi.Child;
import org.subsonic.restapi.ResponseStatus;
import org.subsonic.restapi.Songs;

import click.dobel.alexasonic.configuration.SubsonicCredentials;
import click.dobel.alexasonic.restclient.requestbuilders.RequestBuilders;
import click.dobel.alexasonic.restclient.responseconverters.ResponseConverters;
import click.dobel.alexasonic.test.AlexaSonicIntegrationTest;

@Ignore
public class SubsonicRestClientTest extends AlexaSonicIntegrationTest {

    @Autowired
    private SubsonicRestClient restClient;

    private SubsonicCredentials credentials;

    @Before
    public void createCredentials() {
        final String airsonicUrl = docker //
                .containers().container(CONTAINER_AIRSONIC).port(CONTAINER_AIRSONIC_PORT) //
                .inFormat("http://$HOST:$EXTERNAL_PORT/");

        final SubsonicCredentials credentials = new SubsonicCredentials();
        credentials.setUsername("admin");
        credentials.setPassword("admin");
        credentials.setUrl(airsonicUrl);
        this.credentials = credentials;
    }

    //
    // @After
    // public void teardownMockServer() {
    // restTemplate.setRequestFactory(originalRequestFactory);
    // }

    @Test
    public void testPing() {
        final ResponseStatus response = restClient.execute(RequestBuilders.ping(credentials),
                ResponseConverters.ping());
        assertThat(response).isEqualTo(ResponseStatus.OK);
    }

    @Test
    public void testGetArtists() {
        final ArtistsID3 artists = restClient.execute(RequestBuilders.getArtists(credentials),
                ResponseConverters.getArtists());
        assertThat(artists).isNotNull();
        assertThat(artists.getIndex()).hasSize(2);

        assertThat(artists.getIndex().get(0).getName()).isEqualTo("A");
        assertThat(artists.getIndex().get(0).getArtist()).hasSize(1);

        assertThat(artists.getIndex().get(1).getName()).isEqualTo("H");
        assertThat(artists.getIndex().get(1).getArtist()).hasSize(1);
    }

    @Test
    public void testGetArtistsFlat() {
        final List<ArtistID3> artists = restClient.executeAndFlatten(RequestBuilders.getArtists(credentials),
                ResponseConverters.getArtists());
        assertThat(artists).isNotNull();
        assertThat(artists).hasSize(2);
    }

    @Test
    public void testGetRandomSongs() {
        final Songs songs = restClient.execute(RequestBuilders.getRandomSongs(credentials).withSize(10),
                ResponseConverters.getRandomSongs());
        assertThat(songs).isNotNull();
        assertThat(songs.getSong()).hasSize(10);
    }

    @Test
    public void testStream() {
        final Songs songs = restClient.execute(RequestBuilders.getRandomSongs(credentials).withSize(10),
                ResponseConverters.getRandomSongs());
        final Child song = songs.getSong().get(0);
        final String songId = song.getId();

        System.out.println(RequestBuilders.stream(credentials).withId(songId).getUri());

    }

}